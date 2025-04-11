#!/usr/bin/python3

import hashlib
import os
import re
import subprocess
import sys
from typing import Dict, List

# Allow export relative module: sources_list
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

import sources_list

PATH_TO_JNI_GENERATOR = "gen_script/jni_generator.py"
JNI_ESSENTIAL = "jni_generator_native-essential"
LOCAL_BUILD_ROOT = "../../../../../.."
ROOT = "../.."

CALLED_BY_NATIVE = "@CalledByNative"
JNI_NAMESPACE = "@JNINamespace"

HISTORY_SRC_SEPARATOR = ":"


def maybe_jni_file(java_file: str) -> bool:
    with open(java_file, "r") as f:
        java_lines = f.readlines()
    return any(CALLED_BY_NATIVE in line or JNI_NAMESPACE in line for line in java_lines)


def is_relevant(file: str, history: Dict[str, str]) -> bool:
    hashed_name = hashlib.md5(file.encode("utf-8")).hexdigest()
    modified_time = os.path.getmtime(file)
    historical_modified_time = float(history.get(hashed_name, "0").strip())
    if modified_time <= historical_modified_time:
        return True
    history[hashed_name] = modified_time
    return False


def try_load_history() -> Dict[str, str]:
    history = {}
    history_list = []
    history_path = f"{os.path.dirname(__file__)}/._history"
    if os.path.exists(history_path):
        with open(history_path, "r") as history_file:
            history_list = history_file.readlines()
    for src in history_list:
        if HISTORY_SRC_SEPARATOR in src:
            key, value = src.split(HISTORY_SRC_SEPARATOR)
            history[key] = value
    return history


def save_history(history: Dict[str, str]):
    history_path = f"{os.path.dirname(__file__)}/._history"
    if len(history) > 0:
        with open(history_path, "w") as f:
            for key, value in history.items():
                f.write(f"{key}:{value}\n")


def process(java_files: List[str], is_local_build: bool) -> None:
    history = try_load_history() if is_local_build else {}

    args = ["python3", f"{os.path.dirname(__file__)}/{PATH_TO_JNI_GENERATOR}"]
    for source_file in java_files:
        project_root = f"{os.path.dirname(source_file)}/{LOCAL_BUILD_ROOT}"
        build_dir = os.path.join(project_root, "build/generated/jni/includes")
        java_file = source_file
        if not maybe_jni_file(java_file):
            continue
        if is_local_build and is_relevant(java_file, history):
            continue
        args += ["--input_file", java_file, "--output_file"]
        java_basename = os.path.basename(java_file)
        snake_java_basename = re.sub(r"(?<!^)(?=[A-Z])", "_", java_basename).lower()
        header_name = os.path.splitext(snake_java_basename)[0] + "_jni.h"
        header_file = f"{build_dir}/{header_name}"
        args.append(header_file)

        if os.path.exists(header_file):
            os.remove(header_file)

        print(args)
        subprocess.run(args)

    if is_local_build:
        # TODO(developer): Bring back gen history
        # save_history(history)
        pass


if __name__ == "__main__":
    build_root = sys.argv[1] if len(sys.argv) > 1 else os.path.abspath(__file__ + ROOT)
    is_local_build = not (len(sys.argv) > 1)
    print("Generating JNI stubs...", build_root)
    process(sources_list.sources, is_local_build)
