package com.example.simplejni;

/**
 * Copy of {@link HelloStachka.kt} to generate JNI headers via `javac -h`.
 * <p>
 * See https://youtrack.jetbrains.com/issue/KT-35127
 */
public final class HelloStachka {
    private static native String sayHello(String name);
}
