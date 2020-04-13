#! /bin/bash

snippets=$1

synonym_regex="\[synonym (.|\n)*\]"
definition_regex="<\".*\">"
line_comment_regex="\/\/.*"
block_comment_regex="" # TODO!
empty_line_regex="^[[:space:]]*$"

for file in "$snippets"/*.snippet
do
  [[ -e "$file" ]] || break  # handle the case of no *.wav files
  echo "$file"

  # remove definitions
  sed -i '' -E "s/$definition_regex//g" $file

  # remove synonyms, line comments, and empty lines
  sed -i '' -E "/$synonym_regex|$line_comment_regex|$empty_line_regex/d" $file

  # we use -i '' for compatability issues between different versions of sed
  # see https://stackoverflow.com/questions/16745988/sed-command-with-i-option-in-place-editing-works-fine-on-ubuntu-but-not-mac
done
