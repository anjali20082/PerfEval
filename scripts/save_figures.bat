@echo off
python .\main.py compare_os "5 8" compare_versions 1 dailyhunt
python .\main.py compare_os "5 8" compare_versions 2 dailyhunt
python .\main.py compare_os "5 8" compare_versions 3 dailyhunt
python .\main.py compare_os "5 8" compare_versions 4 dailyhunt

python .\main.py compare_os "5 8" compare_versions 1 googlemaps
python .\main.py compare_os "5 8" compare_versions 2 googlemaps
python .\main.py compare_os "5 8" compare_versions 3 googlemaps
python .\main.py compare_os "5 8" compare_versions 4 googlemaps

python .\main.py compare_os "5 8" compare_versions 1 googlenews
python .\main.py compare_os "5 8" compare_versions 2 googlenews
python .\main.py compare_os "5 8" compare_versions 3 googlenews
python .\main.py compare_os "5 8" compare_versions 4 googlenews

python .\main.py compare_os "5 8" compare_versions 3 youtube
python .\main.py compare_os "5 8" compare_versions 4 youtube
python .\main.py compare_os "5 8" compare_versions 5 youtube

python .\main.py os 5 compare_versions "1 2 3 4" dailyhunt
python .\main.py os 5 compare_versions "1 2 3 4" googlemaps
python .\main.py os 5 compare_versions "1 2 3 4" googlenews
python .\main.py os 5 compare_versions "3 4 5" youtube

python .\main.py os 8 compare_versions "1 2 3 4" dailyhunt
python .\main.py os 8 compare_versions "1 2 3 4" googlemaps
python .\main.py os 8 compare_versions "1 2 3 4" googlenews
python .\main.py os 8 compare_versions "3 4 5" youtube