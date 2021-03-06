# Neural-Net
A small Java library to run and train neural networks using backpropagation and genetic algorithms.

You can download and play with a runnable jar [here](http://harrisongreen.me/GreenGeneticEvolution.jar)

*Note: this was made quickly and for fun, please don't try to use this in any important project*

Examples are provided in green.neural.net.main.

# Using neural net
Packages needed:
- green.neural.net

Documentation is provided in the files.

# Using the genetic evolution simulator
*Working but still under development and no documentation*

Controls:
- arrow keys : change view
- space : move camera to origin
- 0/9 : zoom in/out
- 8 : toggle max speed (doesn't wait between frames)
- 7/6 : increase/decrease speed
- 5 : run at real time
- u : toggle scene rendering (use this with max speed)
- f : lock the camera to the leader
- 1 : toggle fullscreen

Packages needed:
- green.neural.net
- green.neural.net.ga

Libraries needed:
- [Jbox2d](https://github.com/jbox2d/jbox2d)

View the demos in Main.java

# Using the backpropagation algorithms
*A little broken and under development*

Packages needed:
- green.neural.net
- green.neural.net.bp

Run NetTrainer.java to see a 2-3-1 neural network trained to become an XOR gate.
