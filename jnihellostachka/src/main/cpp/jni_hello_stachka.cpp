#include <string>
#include "jni_hello_stachka_jni.h"

using namespace chromium::android;

static std::string sayHello(const std::string &name) {
    return "Hello Stachka! From " + name + " with JNI Generator!";
}

ScopedJavaLocalRef<jstring> JNI_JniHelloStachka_SayHello(JNIEnv *env,
                                                         const JavaParamRef<jobject> &jcaller,
                                                         const JavaParamRef<jstring> &jname) {
    const std::string &name = (env)->GetStringUTFChars(jname.obj(), NULL);
    const auto jGreeting = env->NewStringUTF(sayHello(name).c_str());
    Java_JniHelloStachka_logCall(env, jcaller);
    return ScopedJavaLocalRef<jstring>::Adopt(env, jGreeting);
}
