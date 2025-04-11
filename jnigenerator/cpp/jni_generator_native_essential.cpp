//
// Created by Xuanyi Huang on 1/20/22.
//

#include "jni_generator_native_essential.h"

namespace chromium {
    namespace android {

        JNIEnv* JavaRef<jobject>::SetNewLocalRef(JNIEnv* env, jobject obj) {
            if (obj) {
                obj = env->NewLocalRef(obj);
            }
            if (obj_) {
                env->DeleteLocalRef(obj_);
            }
            obj_ = obj;
            return env;
        }

        void JavaRef<jobject>::SetNewGlobalRef(JNIEnv* env, jobject obj) {
            if (obj) {
                obj = env->NewGlobalRef(obj);
            }
            if (obj_) {
                env->DeleteGlobalRef(obj_);
            }
            obj_ = obj;
        }

        void JavaRef<jobject>::ResetLocalRef(JNIEnv* env) {
            if (obj_) {
                env->DeleteLocalRef(obj_);
                obj_ = nullptr;
            }
        }

        void JavaRef<jobject>::ResetGlobalRef() {
            if (obj_) {
//                TODO(developer): Reset GlobalRef
//                env->DeleteGlobalRef(obj_);
                obj_ = nullptr;
            }
        }

        jobject JavaRef<jobject>::ReleaseInternal() {
            jobject obj = obj_;
            obj_ = nullptr;
            return obj;
        }

        JavaRef<jobject>::JavaRef(JNIEnv* env, jobject obj)
                : obj_(obj)
        {
            if (obj) {
                DDCHECK(env && env->GetObjectRefType(obj) == JNILocalRefType);
            }
        }

    } // namespace android
} // namespace chromium
