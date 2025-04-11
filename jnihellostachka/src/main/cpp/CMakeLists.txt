cmake_minimum_required(VERSION 3.22.1)

project(JniHelloStachka)

include_directories(
        ../../../build/generated/jni/includes
        ../../../../jnigenerator/cpp
)

file(GLOB jnigenerator_essentials
        "../../../../jnigenerator/cpp/*.h"
        "../../../../jnigenerator/cpp/*.cpp"
)

add_library(
        # Specifies the name of the library.
        jnihellostachka

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        jni_hello_stachka.cpp ${jnigenerator_essentials})

find_library(
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        jnihellostachka

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
