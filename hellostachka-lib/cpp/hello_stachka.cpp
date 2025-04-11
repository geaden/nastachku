#include <string>
#include "hello_stachka.h"

static std::string sayHello(const std::string &name) {
    return "Hello Stachka! From " + name + " with Native!";
}

jstring Java_com_example_simplejni_HelloStachka_sayHello(JNIEnv *env, jclass, jstring jName) {
    const auto name = (env)->GetStringUTFChars(jName, nullptr);
    env->ReleaseStringUTFChars(jName, name);

    jclass cls = env->FindClass("com/example/simplejni/HelloStachka");
    jmethodID mid = env->GetStaticMethodID(cls, "logCall", "()V");
    env->CallStaticVoidMethod(cls, mid);

    return env->NewStringUTF(sayHello(name).c_str());
}
