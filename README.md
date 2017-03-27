# sas-convert
Simple SAS7BDAT to CSV conversion tool based on the [Parso library](http://lifescience.opensource.epam.com/parso.html)
and [opencsv](http://opencsv.sourceforge.net).

### Download
The latest version can be downloaded here:
[sas-convert-0.3.jar](https://github.com/thehyve/sas-convert/releases/download/0.3/sas-convert-0.3.jar).

```bash
# Download sas-convert
curl https://github.com/thehyve/sas-convert/releases/download/0.3/sas-convert-0.3.jar -o sas-convert-0.3.jar
```

### Install in home directory
```bash
cp sas-convert-0.3.jar ~/bin/sas-convert.jar
chmod +x ~/bin/sas-convert.jar
```
If your `~/bin` directory is not in your path, add these lines to `~/.profile`:
```bash
if [ -d "$HOME/bin" ] ; then
    PATH="$HOME/bin:$PATH"
fi
```
Usage:
```bash
sas-convert.jar <file.sas> <file.csv>
```

### Build and run from source
```bash
mvn package
java -jar target/sas-convert-0.3.jar <file.sas7bdat> <file.csv>
```
