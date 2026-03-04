dependencies {
    implementation(project(":core"))

    implementation(libs.bundles.custom.libraries)

    implementation(libs.bundles.spring.boot.database)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.redis)

    implementation(libs.spring.boot.starter.feign)
    implementation("commons-fileupload:commons-fileupload:1.6.0")
    implementation(libs.spring.boot.starter.kafka)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
    implementation("org.apache.commons:commons-pool2:2.13.1")
}
