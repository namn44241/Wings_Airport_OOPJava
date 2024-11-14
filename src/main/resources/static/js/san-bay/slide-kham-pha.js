const slides1 = document.querySelectorAll(".slide");
const slider = document.querySelector(".slider");
const dotsContainer1 = document.querySelector(".dots1");
let currentSlide = 0;
const slidesPerPage = 4;

// Calculate actual number of dots needed based on unique slides
const uniqueSlides = Array.from(
  new Set(Array.from(slides1).map((slide) => slide.querySelector("img").src))
).length;
const numDots = Math.ceil(uniqueSlides / slidesPerPage);

// Clear and create new dots
dotsContainer1.innerHTML = "";
for (let i = 0; i < numDots; i++) {
  const dot = document.createElement("span");
  dot.classList.add("dot");
  if (i === 0) {
    dot.classList.add("active");
  }
  dotsContainer1.appendChild(dot);
}

const dots1 = document.querySelectorAll(".dot");

function showSlide(n) {
  currentSlide = n;
  const slideWidth = slides1[0].offsetWidth + 20;

  // Calculate maximum translation
  const maxTranslate = -(uniqueSlides - slidesPerPage) * slideWidth;

  // Calculate desired translation
  let translateX = -n * slidesPerPage * slideWidth;

  // Ensure we don't translate beyond the last unique slide
  translateX = Math.max(translateX, maxTranslate);

  slider.style.transform = `translateX(${translateX}px)`;

  // Update active dot
  dots1.forEach((dot, index) => {
    dot.classList.toggle("active", index === n);
  });
}

function nextSlide() {
  currentSlide = (currentSlide + 1) % numDots;
  showSlide(currentSlide);
}

// Initialize slider
showSlide(0);

// Auto slide
let slideInterval = setInterval(nextSlide, 3000);

// Handle dot clicks
dots1.forEach((dot, index) => {
  dot.addEventListener("click", () => {
    clearInterval(slideInterval);
    showSlide(index);
    slideInterval = setInterval(nextSlide, 3000);
  });
});
