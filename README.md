Created by Nir Meir, Ashraf Hijazi, Abed Massarwa



# Drone Simulator
The main goal for this project is to try find good solution of small drone, flying inside indoor building without getting hit and crash.
The project fully autonomous 2d drone simulator, this simulator is trying to be realistic as much as it can, with lidar sensors,gyroscope sensor ,optical flow sensor and speed sensor.
We add a little bit noise to each sample to make it more realistic approach.
Basic API with real-time info and also manual controlling.
We also implemented kind of area mapping when the drone fly.
This project written in Java.

## Getting Started

When all files located inside eclipse or any other explorer we have the Maps folder which contains couple of maps with route and obstacles.
- Inside "SimulationWindow" in main we have map object with the path to any map you want to test.
- Inside "Drone" we have path to our image represents the drone itself.
After setting this up it is ready to launch.

## Sensors
- Lidar - check the distance between his spot forward and return the distance if hit, if not return 300 as max sample enabled.
In our project we set 3 lidars - one in front, second 90 degrees, third -90 degrees.
- Gyroscpoe - check the rotation of the drone. (0-360)
- Optical flow - check his location on map.
- Speed - max speed is 2m per second.

## Symbols 
- Yellow mark - mapped area.
- Black circle - his purpose to get some idea from where drone came and simply make some route that his passed.(for navigation)
- Red points - represents the wall point.
- Blue line - his whole route.

## API description
Really simple API with few buttons -
Start/pause button, speed up/down, spin -+30/-+45/-+60/90/180.
- Toggle Map - allows you to hide the real map, entering to "real time" vision.
- Toggle AI - enable/disable AI.

## Map rules
If you wish to add custom map it has to be black/white pixels- black is wall/obstacle, white is safe pass.

## V2 update
- Added return home bottom, by clicking it drone will return to starting point.
- Directed Graph feature added. (JGrapht library required)

## Known bugs
- API might be in different place depends on the map.
- Sometimes drone might crash(hit the black pixels) specially in difficult obstacles.
- Sometimes may be indifferent parameters which causing some pixels override - solution is to re-run project.

## Images
![full search](https://github.com/Ashrafhij/Ex1---DroneSimulator/assets/24902621/585928ea-5c81-429c-b8cd-319ae91812c3)
![manual control](https://github.com/Ashrafhij/Ex1---DroneSimulator/assets/24902621/b1d87504-f97f-4314-b37e-6ebba41a1a80)
![return home](https://github.com/Ashrafhij/Ex1---DroneSimulator/assets/24902621/2c78a592-2161-4b83-9e21-fc892f72aac9)
![controller](https://github.com/Ashrafhij/Ex1---DroneSimulator/assets/24902621/b825c038-d594-4cda-86fb-1876bf7a9694)



