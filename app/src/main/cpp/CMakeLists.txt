cmake_minimum_required(VERSION 3.22.1)

project("swanbsdiffapk")

# 导入头文件
include_directories(${CMAKE_SOURCE_DIR}/bzip)
# 批量导入c文件
file(GLOB bzip ${CMAKE_SOURCE_DIR}/bzip/*.c)
add_library( # Sets the name of the library.
        swanbsdiffapk

        SHARED

        native-lib.cpp
        ${bzip}
        bspatch.c
        bsdiff.c)


find_library( # Sets the name of the path variable.
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        swanbsdiffapk
        ${log-lib})