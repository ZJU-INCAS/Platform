#include <jni.h>
#include "team_educoin_transaction_util_VerifyUtil.h"
#include <iostream>

using namespace std;

JNIEXPORT jboolean JNICALL Java_team_educoin_transaction_util_VerifyUtil_verifyFingerprint(JNIEnv *env, jclass cls, jstring j_str1, jstring j_str2){
    const char *c_str1 = NULL;
    const char *c_str2 = NULL;
    // C写法
    // c_str1 = (*env)->GetStringUTFChars(env, j_str1);
    // c_str2 = (*env)->GetStringUTFChars(env, j_str2);
    // C++写法
    c_str1 = env->GetStringUTFChars(j_str1, 0);
    c_str2 = env->GetStringUTFChars(j_str2, 0);
    if(c_str1 == NULL || c_str2 == NULL){
        return false;
    }
    printf("c_str1: %s ---- c_str2: %s\n",c_str1,c_str2);

    for(int i =0; c_str1[i]; ++i){
        printf("%c_",c_str1[i]);
    }
    printf("\n");
    for(int i =0; c_str2[i]; ++i){
        printf("%c_",c_str2[i]);
    }
    printf("\n");
    cout << "this is jni string test!" << endl;
    // C写法
    // (*env)->ReleaseStringUTFChars(env, j_str1, c_str1);
    // (*env)->ReleaseStringUTFChars(env, j_str2, c_str2);
    // C++写法
    env->ReleaseStringUTFChars(j_str1, c_str1);
    env->ReleaseStringUTFChars(j_str2, c_str2);

    return true;
}