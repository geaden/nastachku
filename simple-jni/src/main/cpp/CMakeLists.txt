# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.22.1)

project(HelloStachka)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

include_directories(../../../build/generated/jni/includes)

add_library( # Specifies the name of the library.
        hellostachka

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        hello_stachka.cpp)

find_library(
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        hellostachka

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
