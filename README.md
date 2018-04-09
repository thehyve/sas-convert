# sas-convert
Simple SAS7BDAT to CSV conversion tool based on the [Parso library](http://lifescience.opensource.epam.com/parso.html)
and [opencsv](http://opencsv.sourceforge.net).

### Download
The latest version can be downloaded here:
[sas-convert-0.4.zip](https://github.com/thehyve/sas-convert/releases/download/0.4/sas-convert-0.4.zip).

```bash
# Download sas-convert
curl -L https://github.com/thehyve/sas-convert/releases/download/0.4/sas-convert-0.4.zip -o sas-convert-0.4.zip
unzip sas-convert-0.4.zip
```

### Usage
Usage:
```bash
./sas-convert/sas-convert <file.sas7bdat> [file.csv]
```

By default, the first three lines of output will describe the columns: labels, names, and formats, respecitively. To only output one line of header information containing just column names, use the `--only-column-names` or `-o` option:
```bash
./sas-convert/sas-convert -o <file.sas7bdat> [file.csv]
```

By default, if you don't pass the second argument, the output is sent to stdout. If instead you'd like to save it to an automatically named file based on the name of the input file, the `--auto-create-csv` or `-a` option will save this output to my-filename.csv:
```bash
./sas-convert/sas-convert -a my-filename.sas7bdat
```

### Build and run from source
```bash
mvn package
./sas-convert <file.sas7bdat> [file.csv]
```


## License

Copyright &copy; 2015&ndash;2018 &nbsp; The Hyve.

sas_convert is licensed under the MIT License. See the file [LICENSE](LICENSE).

