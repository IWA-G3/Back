# Back

## Installation

### Required components :
- Curl
- Unzip 
- Cygwin / Git bash (Windows OS)
- Docker / Docker compose

### Running the script

To set up the dev environment in order to properly run the app, run ```./launchAppDev``` in a bash shell.

If spring can't resolve hostname kafka:9092, add the line
```127.0.0.1 kafka``` in your os hosts file.

To check if the installation succeeded, run ```docker ps```, you should see 4 running containers named
- postgres-covid
- kafka
- zookeeper
- connect