dependencies {
    implementation(libs.bundles.custom.libraries)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.lombok.mapstruct.binding)
}


