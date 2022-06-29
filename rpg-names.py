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
    prog='rpg-names',
    description='Generate character names based on provided files',
    allow_abbrev=False
)

#region Arguments
parser.add_argument(
    'Names',
    metavar='names',
    type=str,
    help='path to a file with a list of names'
)
parser.add_argument(
    'Surnames',
    metavar='surnames',
    type=str,
    help='path to a file with a list of surnames'
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

order_by_group = parser.add_mutually_exclusive_group()
order_by_group.add_argument(
    '-on', 
    '--order-by-name', 
    action='store_true', 
    help='sets rule: for every name take a surname'
)
order_by_group.add_argument(
    '-os', 
    '--order-by-surname', 
    action='store_true',
    default=True,
    help='sets rule: for every surname take a name (default)'
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
    default=',\n',
    help='delimiter separating items in output file (default ",\\n")'
)

args = parser.parse_args()
#endregion

names_path = args.Names
surnames_path = args.Surnames
delimiter = args.delimiter.replace('\\n', '\n')
output_delimiter = args.output_delimiter.replace('\\n', '\n')

#region Checking if files exist
if not os.path.isfile(names_path):
    print('Provided file for names does not exist')
    sys.exit()
if not os.path.isfile(surnames_path):
    print('Provided file for character traits does not exist')
    sys.exit()
#endregion

names = read_from_file(names_path, delimiter)
surnames = read_from_file(surnames_path, delimiter)

#region Shuffling
if not args.no_shuffle:
    if args.order_by_name:
        shuffle(surnames)
    else:
        shuffle(names)
#endregion

if args.order_by_name:
    with open('names_full.txt', 'w') as writer:
        for i in range(len(names)):
            surname = surnames[i % len(surnames)]
            writer.write(names[i] + ' ' + surname + output_delimiter)
else:
    with open('names_full.txt', 'w') as writer:
        for i in range(len(surnames)):
            name = names[i % len(names)]
            writer.write(name + ' ' + surnames[i] + output_delimiter)
