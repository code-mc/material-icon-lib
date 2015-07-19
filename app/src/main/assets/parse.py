def dumpSvg():
	with open("icons.svg") as f:
		with open("out.txt", "w+") as o:
			write = ""
			content = f.read()
			parsed = []
			for i in content.split('glyph-name="'):
				try:
					write += i.split('unicode="')[0].split('"')[0] + "\t"
					write += i.split('unicode="')[1][0:8] + "\n"
				except:
					continue
			o.write(write)
			
def writeXml():
	with open("enum.txt", "r+") as f:
		content = f.read()
		parsed = """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="MaterialIconView">
        <attr name="icon" format="enum">
{0}
        </attr>
    </declare-styleable>
</resources>"""
		write = ""
		arr = list()
		arrhex = list()
		for index, i in enumerate(content.split('\n')):
			try:
				enum = i.split("\t")
				name = enum[0]
				hexcode = enum[1]
				write += """\t\t\t<enum name="{0}" value="{1}"/>\n""".format(name, str(index))
				arr.append(name)
				arrhex.append(hexcode)
			except:
				continue
		with open("enumxml.xml", "w+") as o:
			o.write(parsed.format(write))
		with open("enumjava.java", "w+") as j:
			j.write('{"' + '","'.join(arr) + '"};')
		with open("enumjavahex.java", "w+") as j:
			j.write('{"' + '","'.join(arrhex) + '"};')
		with open("enumjavaenum.java", "w+") as j:
			j.write('{' + ','.join(arr).replace("-","_").upper() + '};')

writeXml()