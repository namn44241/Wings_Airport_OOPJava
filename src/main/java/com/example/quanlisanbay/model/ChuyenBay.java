import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "ChuyenBay")
public class ChuyenBay {
    @Id
    @Column(name = "MaChuyenBay")
    private String maChuyenBay;
    
    @Column(name = "TenSanBayDi")
    private String tenSanBayDi;
    
    @Column(name = "TenSanBayDen")
    private String tenSanBayDen;
    
    @Column(name = "GioDi")
    private String gioDi;
    
    @Column(name = "GioDen")
    private String gioDen;

    // Getters and setters
}