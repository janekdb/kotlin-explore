import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "me.janek"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
    kotlinOptions.languageVersion = "2.0"
}

tasks.withType<JavaCompile>() {
    targetCompatibility = "13"
}

application {
    mainClass.set("life.Main")
}

//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//
//plugins {
//    kotlin("jvm") version "1.4.32"
////    id("org.openjfx.javafxplugin") version "0.0.10"
//    application
//}
//
//group = "me.janek"
//version = "1.0-SNAPSHOT"
//
//repositories {
//    mavenCentral()
//}
//
//dependencies {
//    // Align versions of all Kotlin components
////    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
//    // Use the Kotlin JDK 8 standard library.
////    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//
//    api("junit:junit:4.13")
//    implementation("junit:junit:4.13")
////    testImplementation("junit:junit:4.13")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
//
////    testImplementation(platform("org.junit:junit-bom:5.7.2"))
////    testImplementation("org.junit.jupiter:junit-jupiter")
////
//    testImplementation(kotlin("test"))
//    implementation(kotlin("test-junit"))
////    implementation(kotlin("junit-jupiter"))
//    implementation(kotlin("stdlib-jdk8"))
//}
//
////tasks.test {
////    useJUnit()
////}
//
//tasks.test {
////    useJUnit()
//    useJUnitPlatform()
//    testLogging {
//        events("passed", "skipped", "failed")
//    }
//}
//
////tasks.withType<Test>().configureEach {
////    useJUnitPlatform()
////}
//
//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "15"
//}
//
//application {
//    mainClassName = "MainKt"
//}
//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "15"
//}
//val compileTestKotlin: KotlinCompile by tasks
//compileTestKotlin.kotlinOptions {
//    jvmTarget = "15"
//}
////javafx {
////    version = "11"
////    modules("javafx.controls", "javafx.fxml")
////}
