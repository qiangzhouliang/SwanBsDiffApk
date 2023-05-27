#include <jni.h>
#include <string>

extern "C" {
   extern int p_main(int argc, const char * argv[]);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_swan_swanbsdiffapk_BsPatch_00024Companion_bsPatch(JNIEnv *env, jobject thiz, jstring oldapk_,
                                            jstring patch_, jstring output_) {

    const char *oldApk = env->GetStringUTFChars(oldapk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    const char *argv[] = {"", oldApk, output, patch};
    p_main(4, argv);

    env->ReleaseStringUTFChars(oldapk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}