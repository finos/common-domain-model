#! /bin/bash

snippets=$1
docs=$2
rosetta_files=$3

synonym_regex="\[synonym (.|\n)*\]"
definition_regex="<\".*\">"
line_comment_regex="\/\/.*"
empty_line_regex="^[[:space:]]*$"
space_regex="[[:space:]]"
block_comment_regex="" # TODO!

illegal_constructs=0

illegal_constructs_regex="$synonym_regex|$definition_regex|$line_comment_regex"
for file in "$snippets"/*.snippet
do
  [[ -e "$file" ]] || break  # handle the case of no *.wav files
  if [[ $(cat "$file") =~ $illegal_constructs_regex ]]; then
   illegal_constructs=1
   echo ERROR Snippet [$file] contains illeagal constructs. Comments, definitions, and synonyms are not allowed in code snippets.
   cat "$file"
  fi
done

if [[ $illegal_constructs -ne 0 ]]; then
   echo ERROR Run documentation/source/code-snippets/tools/sanitise.sh to remove illeagal constructs automatically.
   exit 1
fi

all=$(cat "$rosetta_files"/*.rosetta)

# remove definitions, synonyms, line comments, and empty lines
all=$(echo "$all" | sed -E "s/$definition_regex//g" | sed -E "/$synonym_regex|$line_comment_regex|$empty_line_regex/d")

# remove all white space
sanitised=$(echo "$all" | tr -d "$space_regex")

result=0

for file in "$snippets"/*.snippet
do
  if [[ $sanitised != *"$(< "$file" tr -d "$space_regex")"* ]]; then
    echo ERROR Snippet [$file] not in sync with model text.
    cat "$file"
    result=1
  fi
done

exit $result
