from functools import wraps
from flask import Flask, jsonify, request
from flask_mysqldb import MySQL
import jwt
from werkzeug.security import generate_password_hash, check_password_hash
import os
import re
import phonenumbers
import math
app = Flask(__name__)
app.config['SECRET_KEY'] = '59ghf45ngf0xtm5lv0hj5kf4lc4ep6r9'
app.config['MYSQL_HOST'] = '127.0.0.1'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'Server@dm1n'
app.config['MYSQL_DB'] = 'flutterdb'

mysql = MySQL(app)
def token_required(func):
    @wraps(func)
    def decorated(*args, **kwargs):
        token = request.args.get('token')
        if not token:
            return jsonify({'Alert':'token is missing'})
        try:
            payload=jwt.decode(token,app.config['SECRET_KEY'])
        except:
            return jsonify({'Alert':'Invalid Token'})
    return decorated

@app.route('/public')
def public():
    return 'for public'
@app.route('/auth')
@token_required
def auth():
    return 'authenticated'


def distance(lon1, lat1, lon2, lat2):
    lon1, lat1, lon2, lat2 = map(math.radians, [lon1, lat1, lon2, lat2])

    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = math.sin(dlat/2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon/2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a)) 
    r = 6371 
    return c * r

##check
def checkExist(table,user):
    cur0=mysql.connection.cursor()
    if table=="Employer":
        cur0.execute("select * from Employer where R_username = %s",[user])
    else:
        cur0.execute("select * from Employee where U_username = %s",[user])
    user=cur0.fetchone()
    cur0.close()
    if user is None:
        return False
    else:
        return True
      
def retrieveID(table,user):
    cur0=mysql.connection.cursor()
    if table=="Employer":
        cur0.execute("select * from Employer where R_username = %s",[user])
    else:
        cur0.execute("select * from Employee where U_username = %s",[user])
    id = cur0.fetchone()
    cur0.close()
    id=id[0]
    return id 


def is_strong_password(password):
    pattern = r'^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$'

    if not re.match(pattern, password):
        return False

    sqli_chars = ['"', "'", ';', '-', '=', '(', ')', '[', ']', '{', '}', '<', '>']
    for char in sqli_chars:
        if char in password:
            return False

    return True
#check
@app.route('/UsignUp', methods=['POST'])
def U_signup():
    fname = request.form['fname']
    lname = request.form['lname']
    email = request.form['email']
    phone = request.form['phone']
    longtitude = float(request.form['longtitude'])
    latitude = float(request.form['latitude'])
    password = request.form['password']
    username = request.form['username']
    if not re.match(r'^\w+$', username) or not re.match(r'^\w+$', fname) or not re.match(r'^\w+$', lname):
        return jsonify({'success':False,'message':'username, first name, and last name can only contain letters, numbers, and underscores'})
    elif not re.match(r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$', email):
        return jsonify({'success':False, 'message':'invalid email'})
    elif len(password)<8 or len(password)>32 or len(email)>32 or len(username)<3 or len(username)>15 or len(phone)>13 or len(fname)>32 or len(lname)>32:
        return jsonify({'success':False,'message':'Oops! make sure that to commit for the field length'})
    elif not is_strong_password(password):
        return jsonify({'success':False,'message':"make sure your password is strong enough and you are not using illegal characters"})
    elif checkExist("Employee",username):
        return jsonify({'success':False,'message':"user already exists"})

    else:

        hashed_password = generate_password_hash(password, method='pbkdf2:sha256', salt_length=8)
        
        cur = mysql.connection.cursor()
        cur.execute("insert into Employee (U_fname,U_lname,U_email,U_phone,U_longtitude,U_latitude,U_password,U_username) values (%s,%s,%s,%s,%s,%s,%s,%s)",([fname],[lname],[email],[phone],[longtitude],[latitude],[hashed_password],[username]))
        mysql.connection.commit()
        cur.close()
        return jsonify({'success':True,'message':'user added'})
#checked
@app.route('/RsignUp', methods=['GET', 'POST'])
def R_signup():
    fname = request.form['fname']
    lname = request.form['lname']
    compName=request.form['compName']
    email = request.form['email']
    phone = request.form['phone']
    longtitude = float(request.form['longtitude'])
    latitude = float(request.form['latitude'])
    password = request.form['password']
    username = request.form['username']
    
    cur1=mysql.connection.cursor()
    cur1.execute("select * from Employer where R_username = %s",[username])
    user=cur1.fetchone()
    cur1.close()
    if not re.match(r'^\w+$', username) or not re.match(r'^\w+$', fname) or not re.match(r'^\w+$', lname):
        return jsonify({'success':False,'message':'username, first name, and last name can only contain letters, numbers, and underscores'})
    elif not re.match(r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$', email):
        return jsonify({'success':False, 'message':'invalid email'})
    elif len(password)<8 or len(password)>32 or len(email)>32 or len(username)<3 or len(username)>15 or len(phone)>13 or len(fname)>32 or len(lname)>32:
        return jsonify({'success':False,'message':'Oops! make sure that to commit for the field length'})
    elif not is_strong_password(password):
        return jsonify({'success':False,'message':"make sure your password is strong enough and you are not using illegal characters"})
    elif user is not None:
        return jsonify({'success':False,'message':'user already exist'})
    else:

        hashed_password = generate_password_hash(password, method='pbkdf2:sha256', salt_length=8)
        
        cur = mysql.connection.cursor()
        cur.execute("INSERT INTO Employer (R_fname,R_lname,R_cname,R_email,R_phone,R_longtitude,R_latitude,R_password,R_username) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)",([fname],[lname],[compName],[email],[phone],[longtitude],[latitude],[hashed_password],[username]))
        mysql.connection.commit()
        cur.close()
        return jsonify({'success':True,'message':'user added'})
#check
@app.route('/UsignIn', methods=['POST'])
def U_signin():
    password = request.form['password']
    username = request.form['username']
    if not re.match(r'^\w+$', username):
        return jsonify({'success':False,'message':'username can only contain letters, numbers, and underscores'})
    elif not is_strong_password(password):
        return jsonify({'success':False,'message':'you are using illegal characters'})
    else:
        cur=mysql.connection.cursor()
        cur.execute("select * from Employee where U_username = %s",[username])
        user=cur.fetchone()
        cur.close()
        if user is None:
            return jsonify({'success':True,'message':'user not found'})
        elif not check_password_hash(user[8], password):
            return jsonify({'success':False,'message':'wrong password'})
        else:
            token = jwt.encode({
                'user':username,
                'type':'employee',
                'firstName':user[1],
                'lastName':user[2]
            },app.config['SECRET_KEY'])
            cur0=mysql.connection.cursor()
            cur0.execute("update Employee set U_token = %s where U_username = %s",([token],[username]))
            mysql.connection.commit()
            cur0.close()
            return jsonify({'success':True,'message':'login succeeded','token':token})
#check
@app.route('/RsignIn', methods=['POST'])
def R_signin():
    password = request.form['password']
    username = request.form['username']
    if not re.match(r'^\w+$', username):
        return jsonify({'success':False,'message':'username can only contain letters, numbers, and underscores'})
    elif not is_strong_password(password):
        return jsonify({'success':False,'message':'you are using illegal characters'})
    else:
        cur=mysql.connection.cursor()
        cur.execute("select * from Employer where R_username = %s",[username])
        user=cur.fetchone()
        cur.close()
        if user is None:
            return jsonify({'success':True,'message':'user not found'})
        elif not check_password_hash(user[12], password):
            return jsonify({'success':False,'message':'wrong password'})
        else:
            token = jwt.encode({
                'user':username,
                'type':'employer',
                'firstName':user[1],
                'lastName':user[2]
            },app.config['SECRET_KEY'])
            cur0=mysql.connection.cursor()
            cur0.execute("update Employer set R_token = %s where R_username = %s",([token],[username]))
            mysql.connection.commit()
            cur0.close()
            return jsonify({'success':True,'message':'login succeeded','token':token})

##checked
@app.route('/retrievePhone',methods=['POST'])
def retrievePhone():
    _type=request.form["type"]
    username=request.form["username"]
    cur =mysql.connection.cursor()
    if _type=="Employer":
        cur.execute("select * from Employer where R_username = %s",[username])
    else:
        cur.execute("select * from Employee where U_username = %s",[username])
    user=cur.fetchone()
    phone=""
    if user is None:
        return jsonify({"success":False,"message":"user does not exists"})
    else:
        if _type=="Employee":
            phone=user[4]
        else:
            phone=user[5]
    return jsonify({"success":True,"message":phone})
##checked
@app.route('/forget',methods=['POST'])
def forget():
    password = request.form["password"]
    user=request.form["username"]
    _type=request.form["type"]
    if len(password)<8 or len(password)>32:
        return jsonify({"success":False,"message":"password doesn\'t match the length"})
    elif not is_strong_password(password):
        return jsonify({"success":False,"message":"weak password"})
    else:
        hashed_password = generate_password_hash(password, method='pbkdf2:sha256', salt_length=8)
        cur = mysql.connection.cursor()
        if _type=="Employee":
            cur.execute("update Employee set U_password = %s where U_username = %s",([hashed_password],[user]))
            mysql.connection.commit()
            cur.close()
        else:
            cur.execute("update Employer set R_password = %s where R_username = %s",([hashed_password],[user]))
            mysql.connection.commit()
            cur.close()
        return jsonify({"success":True,"message":"password updated"})
##check
@app.route('/getInfo',methods=['POST'])
def getInfo():
    token=request.form["token"]
    token = jwt.decode(token,app.config['SECRET_KEY'],algorithms=["HS256"])
    username=token['user']
    _type=token['type']
    fname=token['firstName']
    lname=token['lastName']
    cur=mysql.connection.cursor()
    if _type=="employee":
        cur.execute("select * from Employee where U_username = %s",[username])
        user=cur.fetchone()
        cur.close()
        ##user[3]=email,user[4]=phone
        return jsonify({"success":True,"username":username,"fname":fname,"lname":lname,"email":user[3],"phone":user[4]})
    else:
        cur.execute("select * from Employer where R_username = %s",[username])
        user=cur.fetchone()
        cur.close()
        ##user[4]=email,user[5]=phone,user[3]=cname
        return jsonify({"success":True,"username":username,"fname":fname,"lname":lname,"cname":user[3],"email":user[4],"phone":user[5]})
    
##check  
@app.route("/UpdateR",methods=['POST'])
def UpdateR():
      fname=request.form["fname"]
      lname=request.form["lname"]
      compName=request.form["compName"]
      username=request.form["username"]
      token = jwt.encode({
                'user':username,
                'type':'employer',
                'firstName':fname,
                'lastName':lname
            },app.config['SECRET_KEY'])
      cur=mysql.connection.cursor()
      cur.execute("update Employer set R_fname = %s, R_lname = %s, R_cname = %s, R_token = %s where R_username = %s",([fname],[lname],[compName],[token],[username]))
      mysql.connection.commit()
      cur.close()
      return jsonify({"success":True,"message":"updated","token":token})
  
##check
@app.route("/UpdateE",methods=['POST'])
def UpdateE():
      fname=request.form["fname"]
      lname=request.form["lname"]
      username=request.form["username"]
      token = jwt.encode({
                'user':username,
                'type':'employee',
                'firstName':fname,
                'lastName':lname
            },app.config['SECRET_KEY'])
      cur=mysql.connection.cursor()
      cur.execute("update Employee set U_fname = %s, U_lname = %s, U_token = %s where U_username = %s",([fname],[lname],[token],[username]))
      mysql.connection.commit()
      cur.close()
      return jsonify({"success":True,"message":"updated","token":token})
#check
@app.route("/PasswordUpdate",methods=['POST'])
def PasswordUpdate():
    _type=request.form["type"]
    username=request.form["username"]
    password=request.form["password"]
    oldPass=request.form["oldPass"]
    cur0=mysql.connection.cursor()
    if _type=="Employer":
        cur0.execute("select * from Employer where R_username = %s",[username])
        user=cur0.fetchone()
        cur0.close()
        if not check_password_hash(user[12], oldPass):
            return jsonify({"success":False,"message":"old password not correct"})
    else:
        cur0.execute("select * from Employee where U_username = %s",[username])
        user=cur0.fetchone()
        cur0.close()
        if not check_password_hash(user[8],oldPass):
            return jsonify({"success":False,"message":"old password not correct"})
 
         
    if len(password)<8 or len(password)>32:
        return jsonify({"success":False,"message":"password doesn\'t match the length"})
    elif not is_strong_password(password):
        return jsonify({"success":False,"message":"weak password"})
    else:
        hashed_password = generate_password_hash(password, method='pbkdf2:sha256', salt_length=8)
        cur = mysql.connection.cursor()
        if _type=="Employee":
            cur.execute("update Employee set U_password = %s where U_username = %s",([hashed_password],[username]))
            mysql.connection.commit()
            cur.close()
        else:
            cur.execute("update Employer set R_password = %s where R_username = %s",([hashed_password],[username]))
            mysql.connection.commit()
            cur.close()
        return jsonify({"success":True,"message":"password updated"})
##check 
@app.route("/EmailUpdate",methods=['POST'])
def EmailUpdate():
    _type=request.form["type"]
    username=request.form["username"]
    email=request.form["email"]
    cur=mysql.connection.cursor()
    if _type=="Employer":
        cur.execute("update Employer set R_email = %s where R_username = %s",([email],[username]))
        mysql.connection.commit()
        cur.close()
    else:
        cur.execute("update Employee set U_email = %s where U_username = %s",([email],[username]))
        mysql.connection.commit()    
        cur.close()
    return jsonify({"success":True,"message":"email updated"})
##check
@app.route('/PhoneUpdate',methods=['POST'])
def PhoneUpdate():
    _type=request.form["type"]
    username=request.form["username"]
    phone=request.form["phone"]
    cur=mysql.connection.cursor()
    if _type=="Employer":
        cur.execute("update Employer set R_phone = %s where R_username = %s",([phone],[username]))
    else:
        cur.execute("update Employee set U_phone = %s where U_username = %s",([phone],[username]))
    mysql.connection.commit()
    cur.close()
    return jsonify({"success":True,"message":"phone updated"})

##check
@app.route("/Generate",methods=['POST'])
def Generate():
    token=request.form["token"]
    token = jwt.decode(token,app.config['SECRET_KEY'],algorithms=["HS256"])
    username=token["user"]
    jobTitle=request.form["jTitle"]
    jDesc=request.form["jDesc"]
    expNeeded=int(request.form["expNeeded"])
    empNeeded=int(request.form["empNeeded"])
    category=request.form["category"]
    days=request.form["days"]
    hours=request.form["hours"]
    cur=mysql.connection.cursor()
    cur.execute("update Employer set R_days = %s, R_hours = %s, R_category = %s, R_numAccepted = %s, R_expneeded = %s, R_jtitle = %s, R_jdescription = %s, R_empneeded = %s where R_username = %s",([days],[hours],[category],[0],[expNeeded],[jobTitle],[jDesc],[empNeeded],[username]))
    mysql.connection.commit()
    cur.close()
    _id=retrieveID("Employer",username)
    cur1=mysql.connection.cursor()
    cur1.execute("delete from Matched where R_ID = %s",([_id]))
    mysql.connection.commit()
    cur1.close()
    return jsonify({"success":True,"message":"request generated"})

##check
@app.route("/FetchJobs",methods=['POST'])
def FetchJobs():
    jobTitle=request.form["jTitle"]
    expNeeded=request.form["expNeeded"]
    category=request.form["category"]
    token=request.form["token"]
    token = jwt.decode(token,app.config['SECRET_KEY'],algorithms=["HS256"])
    username=token["user"]
    cur=mysql.connection.cursor()
    cur.execute("select U_longtitude, U_latitude from Employee where U_username = %s",([username]))
    user=cur.fetchone()
    cur.close()
    lonE=user[0]
    latE=user[1]
    cur1=mysql.connection.cursor()
    cur1.execute("select R_cname, R_longtitude, R_latitude, R_expneeded, R_jtitle, R_jdescription,R_id, R_category, R_days, R_hours from Employer where R_category = %s",([category]))
    employer=cur1.fetchall()
    cur1.close()
    lst=[]
    for i in range(len(employer)):
        if (distance(lonE,latE,employer[i][1],employer[i][2])<10) and (jobTitle==employer[i][4]) and (expNeeded==employer[i][3]):
            lst.append(employer[i])
    return jsonify({"success":True,"message":lst})
##check           
@app.route('/Accept',methods=['POST'])
def Accept():
    token=request.form["token"]
    token = jwt.decode(token,app.config['SECRET_KEY'],algorithms=["HS256"])
    username=token["user"]
    _idE=retrieveID("Employee",username)
    _idR=request.form["R_id"]
    jtitle=request.form["jtitle"]
    jdesc=request.form["jdesc"]
    cname=request.form["cname"]
    cur0=mysql.connection.cursor()
    cur0.execute("select R_empneeded, R_numAccepted, R_days, R_hours from Employer where R_id = %s",([_idR]))
    user=cur0.fetchone()
    days=user[2]
    hours=user[3]
    cur0.close()
    if user[0]==user[1]:
        return jsonify({"success":False,"message":"employees number satisfied"})
    else:
        increment=user[1]+1
        cur1=mysql.connection.cursor()
        cur1.execute("update Employer set R_numAccepted = %s where R_id = %s",([increment],[_idR]))
        mysql.connection.commit()
        cur1.close()
        cur = mysql.connection.cursor()
        cur.execute("INSERT INTO Matched (U_ID,R_ID,Jtitle,Jdesc,c_name,isAccepted,days,hours) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)",([_idE],[_idR],[jtitle],[jdesc],[cname],["1"],[days],[hours]))
        mysql.connection.commit()
        cur.close()
        return jsonify({"success":True,"message":"job accepted"})

##check
@app.route("/viewAccE",methods=['POST'])
def viewAccE():
    token=request.form["token"]
    token = jwt.decode(token,app.config['SECRET_KEY'],algorithms=["HS256"])
    username=token["user"]
    _id=retrieveID("Employee",username)
    cur=mysql.connection.cursor()
    cur.execute("select Jtitle, Jdesc, c_name from Matched where U_ID = %s",([_id]))
    match=cur.fetchall()
    cur.close()
    return jsonify({"success":True,"message":match})

#check
@app.route('/viewAccR',methods=['POST'])
def viewAccR():
    token=request.form["token"]
    token = jwt.decode(token,app.config['SECRET_KEY'],algorithms=["HS256"])
    username=token["user"]
    _idR=retrieveID("Employer",username)
    cur=mysql.connection.cursor()
    cur.execute("select U_ID from Matched where R_ID = %s",([_idR]))
    user=cur.fetchall()
    cur.close()
    lst=[]
    for i in range(len(user)):
        cur0=mysql.connection.cursor()
        ID=user[i][0]
        cur0.execute("select U_fname,U_lname,U_phone, U_longtitude, U_latitude from Employee where U_ID = %s",([ID]))
        emp=cur0.fetchone()
        cur0.close()
        lst.append(emp)
    return jsonify({"success":True,"message":lst})
if __name__=='__main__':
    app.run(debug=True,host="0.0.0.0")