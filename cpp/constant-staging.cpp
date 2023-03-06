#include <jni.h>
#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_android_justordinarymovieapp_utils_JNIUtil_getBaseUrlImpl(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("https://api.themoviedb.org/3/movie/");
}