# Gradle Setup Frontend Plugin
##### A gradle plugin that scaffolds a frontend and provides tasks for bundling.

### [Demo](https://www.slideshare.net/WilliamLindner/gradlesetupfrontendplugin-demo)

### Getting Started
Add the frontend plugin to `build.gradle`
```
plugins {
  id 'cool.william.frontend' version '0.0.5'
}
```

[Scaffold](#scaffolding) the frontend
```
$ ./gradlew setupFrontend
```

[Start](#frontend-start) the frontend in development mode
```
$ ./gradlew frontendStart
```

Build the frontend for distribution
```
$ ./gradlew frontendBuild
```

#### Integrate with Spring Boot
If you wish to use these gradle tasks with Spring Boot, add these lines to `build.gradle`

To start the frontend when Spring Boot starts
```
bootRun.dependsOn frontendStart
```

To build the frontend for distribution when Spring Boot is built into a jar
```
bootJar.dependsOn frontendBuild
```

To configure Spring Boot to reload resources that change. Useful when running in development mode
https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#running-your-application-reloading-resources
```
bootRun {
    sourceResources sourceSets.main
}
```

### LiveReload
In order to get [LiveReload](http://livereload.com/) functionality during development, you need to install the LiveReload browser extension, or use this Index Controller that injects LiveReload into `index.html` during development.
https://github.com/wlindner/spring-boot-livereload-index-controller

### Scaffolding
This plugin scaffolds a frontend meaning that it creates a set of files necessary for a minimal frontend. Right now, only React is supported. 
- `package.json` for managing dependencies, metadata, and defining scripts.
- `frontend/` folder for storing the entry point (`index.js` and `index.html`) and components
- `webpack.config.js` for configuring webpack

Scaffolding is only meant to happen once, at the beginning of the project. After scaffolding, modify whatever you want, this is meant to be a minimal starting point.

### Frontend Start
The `frontendStart` gradle task simply runs `npm run start` which corresponds to the start script in `package.json`. It's perfectly fine to use NPM to run this script, but `frontendStart` has the added benefit of forking into a separate process and running while another task like `bootRun` is still running. That way webpack can run in watch mode continuously bundling the frontend while you develop your app.

### Frontend Build
The `frontendBuild` gradle task runs `npm run build` which builds the frontend in webpack's production mode so that it can be shipped off and deployed.
