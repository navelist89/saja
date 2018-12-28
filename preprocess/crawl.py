
import requests
from bs4 import BeautifulSoup
import sqlite3
import re


url_template = 'http://db.cyberseodang.or.kr/front/sabuList/BookContent.do?mId=m01&bnCode=jti_5a0101&titleId=C%d'

conn = sqlite3.connect("saja.db")
cur = conn.cursor()

def addArticle(id, structure, explanation):
    # id : int
    # structure : string, url
    # explanation : string, text

    sql = "INSERT INTO article (id, structure, explanation)  VALUES(?, ?, ?);"
    try:
        cur.execute(sql, (id, structure, explanation))
    except sqlite3.IntegrityError:
        print("Article %d already exists"%(id))

def handleNewLetter(id, content):
    codepoint = 0
    letter = ''
    busu = ''
    hoig = ''
    explanation = ''
    example = ''

    sql = "INSERT INTO dictionary (codepoint, letter, busu, hoig, explanation, example)  VALUES(?, ?, ?, ?,?,?);"
    
    for line in content.splitlines():
        line = line.strip()
        if line[0]=='[':#new letter
            letter = line[1]
            codepoint = ord(letter)
            busu = line[3]

            toks = re.split(',|;|:', line, 1)
            #toks = line.split(',;:',1)
            hoig = toks[0][5:]

            subs = [toks[1]]
            if toks[1].count(';')>1:
                subs = toks[1].split('.')
            for sub in subs:
                sub = sub.strip()
                if not sub:
                    continue
                toks2 = re.split(';|:',sub)
                explanation = toks2[0]
                if(len(toks2)>1):
                    example = toks2[1]
                
                print("%d || %s || %s || %s || %s || %s"%(codepoint, letter, busu, hoig, explanation, example))
                cur.execute(sql, (codepoint, letter, busu, hoig, explanation, example))
        else:
            print("Unhanlded case "+ line)

            
def addSentence(id, chnletter, postfix, translation):
    sql = "INSERT INTO sentence (chnletter, postfix, translation, article)  VALUES(?, ?, ?, ?);"
    cur.execute(sql, (chnletter, postfix, translation, id))
        

        


for id in range(3, 82):
    req = requests.get(url_template%(id))
    if req.ok:
        src=''
        gusil = ''

        soup = BeautifulSoup(req.text, 'html.parser')
        contents = soup.findAll("div", {"class": "org"})[0].get_text().strip().splitlines()
        for line in contents:
            toks = line.split(' ', 1)
            chnlet = toks[0][:4]
            postfix = toks[0][4:]
            meaning = toks[1].strip()
            print("%d || %s || %s || %s"%(id, chnlet, postfix, meaning))
            addSentence(id, chnlet, postfix, meaning)
            
        
        footnotes = soup.findAll('dl', {'class':'st01'})

        
        for elem in footnotes:
            dt = elem.find('dt')
            
            if dt!=None:
                
                title = dt.get_text()
                
                
                if '문장의구조' in title:
                    img = elem.find('img')
                    src = img['src']
                    #print (src)
                    
                    
                    
                else:
                    dd = elem.find('dd')
                    while True:
                        _dd = dd.find('dd')
                        if _dd==None:
                            break
                        dd = _dd
                    
                    
                    content = dd.get_text()

                    if '신습한자' in title:
                        #handleNewLetter(id, content)
                        pass
                    if '한자의구실' in title:
                        gusil = content
                        
                        
        #addArticle(id, src, gusil)
                    
        print("=======Done %d============"%(id))


conn.commit()
conn.close()