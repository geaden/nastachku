package com.example.jnihellostachka;

import static com.example.logging.LoggingKt.simpleLogging;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.example.jnigenerator.annotations.CalledByNative;
import com.example.jnigenerator.annotations.JNINamespace;
import com.example.libloader.LibLoader;

@Keep
@JNINamespace("")
public class JniHelloStachka implements IHelloStachka {

    private static final String TAG = "JniHelloStachka";

    static {
        LibLoader.loadLibrary("jnihellostachka");
    }

    @NonNull
    @Override
    public String sayHello(@NonNull String name) {
        return nativeSayHello(name);
    }

    @CalledByNative
    private void logCall() {
        simpleLogging(TAG, () -> "Called from native");
    }

    private native String nativeSayHello(@NonNull String name);
}
