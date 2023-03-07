#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_android_justordinarymovieapp_utils_JNIUtil_getBaseUrlImpl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("https://api.themoviedb.org/3/");
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_android_justordinarymovieapp_utils_JNIUtil_getTMDBApiKey(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("d207771f15a19bc3196dded01c49c5b9");
}