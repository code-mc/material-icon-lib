def dumpSvg():
	with open("icons.svg") as f:
		with open("out.txt", "w+") as o:
			write = ""
			content = f.read()
			parsed = []
			for i in content.split('glyph-name="')[1:]:
				try:
					write += i.split('unicode="')[0].split('"')[0] + "\t"
					write += i.split('unicode="')[1][0:8] + "\n"
				except:
					continue
			o.write(write)
			
def writeXml():
	with open("out.txt", "r+") as f:
		content = f.read()

		srtd = list()
		for index, i in enumerate(content.split('\n')):
			try:
				enum = i.split("\t")
				name = enum[0]
				hexcode = enum[1]

				if name in ["import", "package", "switch", "null"]:
					name += "-icon"

				hexcode = hexcode.replace("&#", "0")[:-1]
				srtd.append((int(hexcode, 16), name))
			except:
				continue

		srtd.sort()

		parsed = """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="MaterialIconView">
        <attr name="icon" format="enum">
{0}
        </attr>
    </declare-styleable>
</resources>"""
		write = ""
		write_java_enum = ""
		arr = list()
		arrhex = list()
		print "start code:", srtd[0], hex(srtd[0][0])
		prevhex = srtd[0][0] - 1
		for index, i in enumerate(srtd):
			try:
				name = str(i[1])
				hexcode = str(i[0])

				if i[0] != prevhex + 1:
					print "gap in hexcodes:", prevhex, i[0]
				prevhex = i[0]
				write_java_enum += """\t\t\tpublic static final int {0}={1};\n""".format(name.replace("-","_").upper(), str(index))
				write += """\t\t\t<enum name="{0}" value="{1}"/>\n""".format(name.replace("-","_"), str(index))
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
			#j.write('{' + ','.join(arr).replace("-","_").upper() + '};')
			j.write(write_java_enum)

dumpSvg()
writeXml()