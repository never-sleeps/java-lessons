### Gradle

To start the application:  
`./gradlew build`  
`java -jar ./L01-gradle/build/libs/gradleHelloWorld-0.1.jar`

To unzip the jar:  
`unzip -l L01-gradle.jar`  
`unzip -l gradleHelloWorld-0.1.jar`

Построение дерева зависимостей:  
` ./gradlew Modul-Name:dependencies`

Информация о зависимости:  
`./gradlew :Modul-Name:dependencyInsight --configuration runtimeClasspath --dependency javax.servlet:servlet-api`

