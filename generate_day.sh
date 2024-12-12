#!/bin/bash

day_num=$1
session_token=$2


# Create directory
directory_name=day$day_num
directory_path=./src/main/kotlin/$directory_name
mkdir $directory_path

#Create file
file_path=$directory_path/day$day_num.kt
touch $file_path

# Populate file with default code.
text=$(cat <<'EOF'
package $directory_name

fun part1(): Int {
    return 0
}

fun part2(): Int {
    return 0
}
EOF
)

text="${text//\$directory_name/$directory_name}"

echo "$text" > $file_path

# load input data.
input_file_path="./inputs/day${day_num}_input.txt"

curl -v "https://adventofcode.com/2024/day/${day_num}/input" -H "cookie: session=${session_token}" -o $input_file_path