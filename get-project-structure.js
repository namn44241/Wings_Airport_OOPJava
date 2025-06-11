const fs = require('fs');
const path = require('path');

// Danh sách các thư mục/file cần bỏ qua
const IGNORE_PATTERNS = [
    'node_modules',
    '.git',
    '.next',
    'dist',
    'build',
    '.vscode',
    '.idea',
    '__pycache__',
    '.pytest_cache',
    '.mypy_cache',
    'venv',
    'env',
    '.env',
    '.DS_Store',
    'Thumbs.db',
    '*.log',
    '*.tmp',
    '*.cache',
    '.nuxt',
    '.output',
    'coverage',
    '.nyc_output',
    'artifacts/WebScrape/data*.json',
    'ai/JupiterNotebooks',
];

// Thêm patterns tùy chỉnh vào đây nếu cần
const CUSTOM_IGNORE = [
    'database/data',
    'pg_*',
    // Thêm các file/folder khác muốn bỏ qua
];

// Các thư mục chỉ hiển thị tên, không liệt kê nội dung
const FOLDER_ONLY = [
    'uploads',
    'images',
    'files',
    'static',
    'public/images',
    // Thêm các thư mục khác chỉ cần hiển thị tên
];

const ALL_IGNORE = [...IGNORE_PATTERNS, ...CUSTOM_IGNORE];

function shouldIgnore(itemPath, itemName) {
    // Bỏ qua file không có extension (chỉ là số hoặc tên database)
    const hasExtension = itemName.includes('.');
    const isNumberOnly = /^\d+(_fsm|_vm|_init)?$/.test(itemName);
    const isDbFile = /^(base|global|pg_|PG_)/i.test(itemName);
    
    if (!hasExtension && (isNumberOnly || isDbFile)) {
        return true;
    }
    
    return ALL_IGNORE.some(pattern => {
        if (pattern.includes('*')) {
            const regex = new RegExp(pattern.replace(/\*/g, '.*'));
            return regex.test(itemName) || regex.test(itemPath);
        }
        return itemName === pattern || itemPath.includes(pattern);
    });
}

function shouldSkipContent(itemPath, itemName) {
    const relativePath = path.relative(process.cwd(), itemPath);
    return FOLDER_ONLY.some(pattern => {
        return itemName === pattern || relativePath.endsWith(pattern) || relativePath.includes(pattern);
    });
}

function getProjectStructure(dirPath, prefix = '', maxDepth = 10, currentDepth = 0) {
    if (currentDepth >= maxDepth) return '';
    
    let result = '';
    
    try {
        const items = fs.readdirSync(dirPath).sort();
        const validItems = items.filter(item => {
            const itemPath = path.join(dirPath, item);
            const relativePath = path.relative(process.cwd(), itemPath);
            return !shouldIgnore(relativePath, item);
        });
        
        validItems.forEach((item, index) => {
            const itemPath = path.join(dirPath, item);
            const isLast = index === validItems.length - 1;
            
            // Sử dụng ASCII characters thay vì Unicode box drawing
            const currentPrefix = isLast ? '`-- ' : '|-- ';
            const nextPrefix = isLast ? '    ' : '|   ';
            
            try {
                const stats = fs.statSync(itemPath);
                
                if (stats.isDirectory()) {
                    // Kiểm tra có phải thư mục chỉ hiển thị tên không
                    if (shouldSkipContent(itemPath, item)) {
                        result += `${prefix}${currentPrefix}[DIR] ${item}/ (content skipped)\n`;
                    } else {
                        result += `${prefix}${currentPrefix}[DIR] ${item}/\n`;
                        result += getProjectStructure(
                            itemPath, 
                            prefix + nextPrefix, 
                            maxDepth, 
                            currentDepth + 1
                        );
                    }
                } else {
                    // Thêm type theo loại file
                    const ext = path.extname(item).toLowerCase();
                    let type = '[FILE]';
                    
                    const typeMap = {
                        '.js': '[JS]', '.jsx': '[JSX]', '.ts': '[TS]', '.tsx': '[TSX]',
                        '.py': '[PY]', '.json': '[JSON]', '.md': '[MD]', '.txt': '[TXT]',
                        '.css': '[CSS]', '.scss': '[SCSS]', '.html': '[HTML]', '.vue': '[VUE]',
                        '.png': '[IMG]', '.jpg': '[IMG]', '.jpeg': '[IMG]', '.gif': '[IMG]', '.svg': '[SVG]',
                        '.yml': '[YML]', '.yaml': '[YAML]', '.toml': '[TOML]', '.ini': '[INI]',
                        '.env': '[ENV]', '.gitignore': '[GIT]', '.dockerignore': '[DOCKER]',
                        '.sql': '[SQL]', '.db': '[DB]', '.sqlite': '[DB]',
                    };
                    
                    type = typeMap[ext] || type;
                    result += `${prefix}${currentPrefix}${type} ${item}\n`;
                }
            } catch (err) {
                result += `${prefix}${currentPrefix}[ERROR] ${item} (access denied)\n`;
            }
        });
    } catch (err) {
        result += `${prefix}[ERROR] Error reading directory: ${err.message}\n`;
    }
    
    return result;
}

function main() {
    const projectPath = process.cwd();
    const projectName = path.basename(projectPath);
    
    const isRedirectedOutput = !process.stdout.isTTY;
    
    if (!isRedirectedOutput) {
        console.log(`\nProject Structure: ${projectName}\n`);
        console.log(`Path: ${projectPath}\n`);
        console.log('='.repeat(60));
    }
    
    const structure = getProjectStructure(projectPath);
    console.log(structure);
    
    if (!isRedirectedOutput) {
        console.log('='.repeat(60));
        console.log('\nIgnored patterns:');
        ALL_IGNORE.forEach(pattern => console.log(`  - ${pattern}`));
        console.log('\nTo customize, edit CUSTOM_IGNORE array in script');
        
        // Chỉ lưu file khi KHÔNG redirect output
        try {
            const fileName = 'project-structure.txt';
            const header = `Project Structure: ${projectName}\nPath: ${projectPath}\n${'='.repeat(60)}\n`;
            const footer = `\n${'='.repeat(60)}\nIgnored patterns:\n${ALL_IGNORE.map(pattern => `  - ${pattern}`).join('\n')}\n`;
            
            fs.writeFileSync(fileName, header + structure + footer, 'utf8');
            console.log(`\nStructure saved to: ${fileName}`);
        } catch (err) {
            console.error(`\nError saving file: ${err.message}`);
        }
    }
}

// Chạy script
main(); 