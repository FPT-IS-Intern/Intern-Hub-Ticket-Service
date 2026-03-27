dependencies {
    implementation(project(":core"))

    implementation(libs.bundles.custom.libraries)

    implementation(libs.bundles.spring.boot.database)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.redis)

    implementation(libs.spring.boot.starter.feign)
    implementation(libs.spring.boot.starter.web)
    implementation("commons-fileupload:commons-fileupload:1.6.0")
    implementation(libs.spring.boot.starter.kafka)
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("com.nimbusds:nimbus-jose-jwt:9.37.3")

    implementation(libs.mapstruct)
    implementation(libs.common.library)
    annotationProcessor(libs.mapstruct.processor)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
    implementation("org.apache.commons:commons-pool2:2.13.1")

    implementation("software.amazon.awssdk:s3:2.20.0")
}
