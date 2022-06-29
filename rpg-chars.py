import argparse
from random import shuffle

import os
import sys

def trim_endline(text):
    return text.replace('\r', ' ').replace('\n', ' ').strip()

def trim(text, delimiter):
    trimmed = [trim_endline(s) for s in text.split(delimiter)]
    return [s for s in trimmed if s]

def read_from_file(path, delimiter):
    return trim(''.join(open(path, 'r').readlines()), delimiter)

parser = argparse.ArgumentParser(
    prog='rpg-chars',
    description='Generate characters based on provided files',
    allow_abbrev=False
)

#region Arguments
parser.add_argument(
    'Names',
    metavar='names',
    type=str,
    help='path to a file with names of the characters'
)
parser.add_argument(
    'Traits',
    metavar='traits',
    type=str,
    help='path to a file with list of character traits'
)
parser.add_argument(
    'Others',
    metavar='others',
    type=str,
    nargs='*',
    help='paths to files with lists of other characteristics (eg. professions)'
)

shuffle_group = parser.add_mutually_exclusive_group()
shuffle_group.add_argument(
    '-s', 
    '--shuffle', 
    action='store_true', 
    default=True,
    help='disables shuffling of characteristics (default)'
)
shuffle_group.add_argument(
    '-ns', 
    '--no-shuffle', 
    action='store_true',
    help='disables shuffling of characteristics'
)

parser.add_argument(
    '-d',
    '--delimiter',
    type=str,
    default=',',
    help='delimiter separating items in lists (default ",")'
)

parser.add_argument(
    '-od',
    '--output-delimiter',
    type=str,
    default=', ',
    help='delimiter separating names, traits and other characteristics in the output file (default ", ")'
)

args = parser.parse_args()
#endregion

names_path = args.Names
traits_path = args.Traits
other_paths = args.Others
delimiter = args.delimiter.replace('\\n', '\n')
output_delimiter = args.output_delimiter.replace('\\n', '\n')

#region Checking if files exist
if not os.path.isfile(names_path):
    print('Provided file for names does not exist')
    sys.exit()
if not os.path.isfile(traits_path):
    print('Provided file for character traits does not exist')
    sys.exit()
if not all(os.path.isfile(path) for path in other_paths):
    print('Provided additional files do not exist')
    sys.exit()
#endregion

names = read_from_file(names_path, delimiter)
traits = read_from_file(traits_path, delimiter)
others = [read_from_file(path, delimiter) for path in other_paths]

#region Shuffling
if not args.no_shuffle:
    shuffle(names)
    shuffle(traits)
    [shuffle(l) for l in others]
#endregion

characteristics = [names] + [traits] + others
with open('characters.txt', 'w') as writer:
    for i in range(len(names)):
        charac = [c[i % len(c)] for c in characteristics]
        writer.write(output_delimiter.join(charac) + '\n')
