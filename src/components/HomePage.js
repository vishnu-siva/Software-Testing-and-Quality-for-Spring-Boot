import React from "react";
import { Form, Link } from "react-router-dom";
import "../styles/HomePage.css";
import barbellbiceps from "../images/barbellbiceps.gif";
import bububububear from "../images/bububububear.gif";
import pengupudgy from "../images/pengupudgy.gif";
import tkbubududu from "../images/tkbubududu.gif";
import tkthaobubududu from "../images/tkthaobubududu.gif";
import istockphoto from "../images/istockphoto.jpg";
import istockphoto2 from "../images/istockphoto2.jpg";
import heavy from "../images/heavy.jpg";
import hiitworkout from "../images/hiitworkout.jpg";
import high from "../images/high.jpg";
import minut from "../images/minut.jpg";
import landscape from "../images/landscape.jpg";
import resistance from "../images/resistance.jpg";
import cable from "../images/cable.jpg";
import two from "../images/two.jpg";
import one from "../images/one.jpg";
import Optimize from "../images/Optimize.jpg";
import Green from "../food/Green.jpg";
import five from "../food/five.jpg";
import shutterstock from "../food/shutterstock.jpg";
import Keto from "../food/Keto.jpg";
import spinach from "../food/spinach.jpg";
import almond from "../food/almond.jpg";
import roasted from "../food/roasted.jpg";
import Banana from "../food/Banana.jpg";
import lean from "../food/lean.jpg";
import veggie from "../food/veggie.jpg";
import SweetPotato from "../food/SweetPotato.jpg";
import Grilled from "../food/Grilled.jpg";
import logo from "../images/logo.png";


function HomePage() {
  return (
    <div className="container">
       <nav >
       <div>
        <Link to="/login">
          <button>LOG IN</button>
        </Link>
      </div>

       <div style={{ display: "flex", gap: "40px" }}>
        <button onClick={() => alert('Learn more about FitEase!')}>ABOUT</button>
        <button onClick={() => alert('Contact us at: contact@fitease.com')}>CONTACT US</button>
        </div> 
      </nav>
      
      <div className="subtext" > Make<br />Your Body<br />Goals...</div>
      <div className="curve-container">
        
        <img src={barbellbiceps} alt="barbellbiceps"  className="curve-img barbellbiceps" />
        <img src={bububububear} alt="bububububear" className="curve-img bububububear" />
        <img src={pengupudgy} alt="pengupudgy" className="curve-img pengupudgy" />
        <img src={tkbubududu} alt="tkbubududu" className="curve-img tkbubududu" />
         <img src={tkthaobubududu} alt="tkthaobubududu" className="curve-img tkthaobubududu" />
        
      </div>

      <h1 className="title">FitEase</h1>
      <div className="subtitle">Personalized Workout Plans</div>

      
      <div className="section-title">Power Surge</div>
      <p className="section-description">Strength training and heavy lifts.</p>
      <div className="image-row">
        <img src={istockphoto} alt="Workout 1" />
        <img src={istockphoto2} alt="Workout 2" />
        <img src={heavy} alt="Workout 3" />
      </div>
      <p className="description-text">
        Strength training involves exercises that focus on improving muscle strength, endurance, and overall performance. This type of workout typically includes compound movements such as squats, deadlifts, bench presses, and other multi-joint exercises that engage multiple muscle groups. Power Surge is designed to help you build lean muscle mass, increase overall strength, and enhance physical performance. Whether you're a beginner or an experienced lifter, this program can be easily adapted to suit your fitness level.
      </p>

      
      <div className="section-title">Cardio Blast</div>
      <p className="section-description">High-Intensity Cardio Workouts</p>
      <div className="image-row">
        <img src={hiitworkout} alt="Cardio 1" />
        <img src={high} alt="Cardio 2" />
        <img src= {minut} alt="Cardio 3" />
      </div>
      <p className="description-text">
      Cardio Blast is designed to get your heart pumping and burn calories quickly. This high-energy workout combines a variety of cardio exercises, including jumping jacks, burpees, mountain climbers, and running drills. The program is perfect for improving cardiovascular health, accelerating fat loss, and boosting overall endurance. Each session is carefully structured to push your limits while keeping you motivated, energized, and fully engaged throughout the workout.
      </p>

      <div className="section-title">Power Abs</div>
      <p className="section-description">Targeted Ab Workouts for Building Core Strength</p>
      <div className="image-row">
        <img src={landscape} alt="Abs 1" />
        <img src={resistance} alt="Abs 2" />
        <img src={cable} alt="Abs 3" />
      </div>
      <p className="description-text">
      Power Abs is a workout program designed to specifically target and strengthen your abdominal muscles and core. The routine includes a variety of effective exercises such as planks, crunches, leg raises, Russian twists, and bicycle crunches. Building a strong core is essential for improving overall fitness, balance, posture, and stability. These workouts will help you develop a powerful, functional core that not only enhances your daily activities but also boosts performance in other fitness programs and sports.
      </p>

   
      <div className="section-title">HIIT It Hard</div>
      <p className="section-description">High-Intensity Interval Training (HIIT)</p>
      <div className="image-row">
        <img src={two} alt="HIIT 1" />
        <img src={one} alt="HIIT 2" />
        <img src={Optimize} alt="HIIT 3" />
      </div>
      <p className="description-text">
      HIIT (High-Intensity Interval Training) is a fast-paced, high-energy workout that alternates between short bursts of intense activity and brief recovery periods. This training method is highly effective for burning calories, improving cardiovascular fitness, boosting endurance, and building lean muscle. HIIT workouts are time-efficient, allowing you to achieve maximum results in minimal time. Perfect for busy schedules, these sessions are designed to push your limits, keep you engaged, and deliver amazing results.
      </p>

      
      <div className="recipe-title">Healthy Recipes</div>
      <p className="description-text">
       Healthy food plays a crucial role in supporting your workout routine and helping you achieve your fitness goals. These carefully curated recipes provide the perfect balance of nutrients to fuel your body before workouts and aid in faster recovery afterward. Each recipe is designed to be delicious, nutritious, and easy to prepare, making it effortless to maintain a healthy, balanced diet that complements your fitness journey.
      </p>

      <div className="recipe-gallery">
        <div className="recipe-item">
          <img src={Green} alt="Smoothie" />
          <p>Protein-Packed Power Smoothie</p>
        </div>
        <div className="recipe-item">
          <img src={lean} alt="Salad" />
          <p>Lean Green Salad</p>
        </div>
        <div className="recipe-item">
          <img src={veggie} alt="Stir-Fry" />
          <p>Veggie Quinoa Stir-Fry</p>
        </div>
        <div className="recipe-item">
          <img src={SweetPotato} alt="Sweet Potato" />
          <p>Baked Sweet Potato Fries</p>
        </div>
        <div className="recipe-item">
          <img src={Grilled} alt="Chicken" />
          <p>Grilled Chicken with Avocado Salsa</p>
        </div>
        <div className="recipe-item">
          <img src={five} alt="Zoodles" />
          <p>Zucchini Noodles with Pesto</p>
        </div>
        <div className="recipe-item">
          <img src={shutterstock} alt="Bowl" />
          <p>Berry Antioxidant Breakfast Bowl</p>
        </div>
        <div className="recipe-item">
          <img src={Keto} alt="Cauli Rice" />
          <p>Low-Carb Cauliflower Rice</p>
        </div>
        <div className="recipe-item">
          <img src={spinach}alt="Omelette" />
          <p>Spinach and Feta Omelette</p>
        </div>
        <div className="recipe-item">
          <img src={almond} alt="Chia" />
          <p>Chia Seed Pudding with Almond Butter</p>
        </div>
        <div className="recipe-item">
          <img src={roasted} alt="Salmon" />
          <p>Salmon with Lemon and Asparagus</p>
        </div>
        <div className="recipe-item">
          <img src={Banana} alt="Pancakes" />
          <p>Healthy Banana Pancakes</p>
        </div>
      </div>

      <div className="sectionh"> <h2> Motivation and Tips </h2> </div>
      <div className="tips-section"> 
        <p><strong>Stay Consistent:</strong> Progress is built one step at a time. Keep showing up, even on the tough days.</p>
        <p><strong>Set Achievable Goals:</strong> Break your fitness goals into smaller, realistic milestones to stay motivated.</p>
        <p><strong>Track Your Progress:</strong> Celebrate every victory, big or small. Seeing progress keeps you focused.</p>
        <p><strong>Find a Workout Buddy:</strong> Having a partner can make workouts more fun and help keep you accountable.</p>
        <p><strong>Listen to Your Body:</strong> Rest when you need it and push yourself when you're ready. Balance is key to long-term success.</p>
        <p><strong>Positive Mindset:</strong> Believe in yourself. Your mind is just as important as your body in achieving fitness goals.</p>
      </div>

     
      <footer>
        <small>UPSKILL FOR A BETTER FUTURE</small>
        <h3>Request More Information</h3>
        <p>Vish Media, SVS is a clinical-stage healthcare company which is developing a unique.</p>
        <button>Contact Us</button>
        <div className="footer-bottom">
          © 2025 Vish Media, SVS
        </div>
        <div className="footer-links">
          <a href="#">Team</a>
          <a href="#">Case Studies</a>
          <a href="#">Publications</a>
        </div>
        <div className="footer-logo">
          <img src={logo} alt="Footer Logo" height="30" />
        </div>
      </footer>
    </div>

    
  );
}

export default HomePage;