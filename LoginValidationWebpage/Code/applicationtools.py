"""
__filename__ = "applicationtools.py"
__coursename__ = "SDEV 300 6380 - Building Secure Web Applications (2198)"
__author__ = "John Kucera"
__copyright__ = "None"
__credits__ = ["John Kucera"]
__license__ = "GPL"
__version__ = "1.0.0"
__maintainer__ = "John Kucera"
__email__ = "johnkucera00@gmail.com"
__status__ = "Test"
"""
from datetime import datetime
import hashlib
import json
from multiprocessing import Value
import time
from flask import Flask, render_template, request, redirect, url_for
from wtforms import Form, StringField, PasswordField
from wtforms.validators import DataRequired
from ip2geotools.databases.noncommercial import DbIpCity
from pytz import timezone

# App is flask
APP = Flask(__name__)

# Initializing Global Variables.
# Timer starts HERE for log analyzer (accounting for more than 10 failed attempts
# in less than 5 minutes)
ATTEMPTCOUNT = Value('i', 0)
STARTTIMESECONDS = Value('d', time.time())
STARTTIME15ATTEMPTS = Value('d', 0)

# NOTE: I "commented out" the part where the preset username/password are
# written to savedPasswords.json. They are in the savedPasswords.json file already -
# BUT if the tester wants, they can run the code here so that savedPasswords.json
# is reset to username = "Bob", password = HelloWorld1234@!. Said code is right below:

# PRESETUSER = {"username": "Bob", "password": "bbc45529d7f66aee37ecad5b446a4b05"}
# with open('Week8Deliverables/savedPasswords.json', 'w') as savedpwfile:
#     savedpwfile.seek(0)
#     json.dump(PRESETUSER, savedpwfile)

# Class to get form data
class LoginForm(Form):
    """
    Class for reuseable HTML form
    """
    Form.username = StringField('Username', validators=[DataRequired()])
    Form.password = PasswordField('Password', validators=[DataRequired()])

# Main login page
@APP.route('/', methods=['GET'])
def index():
    """
    Main login form request
    """
    # Getting form
    form = LoginForm(request.form)

    # Opening log file (only if program is just starting)
    if STARTTIME15ATTEMPTS.value == 0:
        ipaddress = request.environ['REMOTE_ADDR']
        log = {'IP Address': ipaddress, 'attemptcount': ATTEMPTCOUNT.value}
        with open('Week8Deliverables/log.json', 'w') as logfile:
            logfile.seek(0)
            json.dump(log, logfile)

    return render_template('loginForm.html', form=form)

# Processing login
@APP.route('/process_login', methods=['POST'])
def process_login():
    """
    Processing login
    """
    error_msg = ""
    ipaddress = request.environ['REMOTE_ADDR']

    # Get form data
    form = LoginForm(request.form)
    if request.method == 'POST' and form.validate():
        username = request.form['username']
        password = request.form['password']
        password_encoded = hashlib.md5(password.encode()).hexdigest()

        # Keeping track of time
        currenttimeseconds = time.time()
        eastern = timezone('US/Eastern')
        rightnow = datetime.now(eastern)
        today = datetime.today()
        currenttimeest = str(rightnow.strftime('%H:%M:%S'))
        currentdateest = str(today.strftime("%B %d, %Y"))

        # After 15 failed attempts, deny login for 5 minutes
        if currenttimeseconds <= STARTTIME15ATTEMPTS.value + 300:
            STARTTIME15ATTEMPTS.value = currenttimeseconds
            with open('Week8Deliverables/log.json', 'r+') as logfile:
                logdict = json.load(logfile)
                logdict['attemptcount'] += 1
                failedstring = 'Failed Attempt'
                attempt = [ipaddress, currenttimeest, currentdateest]
                logdict.update({failedstring + str(logdict['attemptcount']): ', '.join(attempt)})
                logfile.seek(0)
                json.dump(logdict, logfile)
                if currenttimeseconds < STARTTIMESECONDS.value + 300 and logdict['attemptcount'] > 10:
                    failedstring = 'Failed Attempt'
                    geolocate = DbIpCity.get(ipaddress)
                    print(ipaddress, 'had', logdict['attemptcount'], 'failed login attempts'
                          ' in a 5 minute period at',
                          logdict[failedstring + str(logdict['attemptcount'])].strip(ipaddress + ','))
                    print(ipaddress, 'has a Lat/Long of', geolocate.latitude, '/', geolocate.longitude)
            error_msg = 'It has not been at least 5 minutes since previous failed attempt. Please wait 5 minutes.'
            return render_template('loginForm.html', form=form, error_msg=error_msg)

        # Login validation
        with open('Week8Deliverables/savedPasswords.json', 'r') as savedpwfile:
            savedpwdict = json.load(savedpwfile)

            # Username check
            if username == savedpwdict['username']:

                # Password check
                if password_encoded == savedpwdict['password']:

                    # Redirect to success page, clear failed login attempts
                    with open('Week8Deliverables/log.json', 'r+') as logfile:
                        logdict = json.load(logfile)
                        logdict['attemptcount'] = 0
                        logfile.seek(0)
                        json.dump(logdict, logfile)
                    return render_template('loginProcessed.html', form=form,
                                           username_data=username, password_data=password)

                # Password failed
                else:
                    with open('Week8Deliverables/log.json', 'r+') as logfile:
                        logdict = json.load(logfile)
                        logdict['attemptcount'] += 1
                        failedstring = 'Failed Attempt'
                        attempt = [ipaddress, currenttimeest, currentdateest]
                        logdict.update({failedstring + str(logdict['attemptcount']): ', '.join(attempt)})
                        logfile.seek(0)
                        json.dump(logdict, logfile)
                    error_msg = "Password is incorrect. Please try again."

            # Username failed
            else:
                with open('Week8Deliverables/log.json', 'r+') as logfile:
                    logdict = json.load(logfile)
                    logdict['attemptcount'] += 1
                    failedstring = 'Failed Attempt'
                    attempt = [ipaddress, currenttimeest, currentdateest]
                    logdict.update({failedstring + str(logdict['attemptcount']): ', '.join(attempt)})
                    logfile.seek(0)
                    json.dump(logdict, logfile)
                error_msg = "Username does not exist. Please try again."

        # Printing log if more than 10 failed attempts in past 5 minutes
        with open('Week8Deliverables/log.json', 'r') as logfile:
            logdict = json.load(logfile)
            if currenttimeseconds < STARTTIMESECONDS.value + 300 and logdict['attemptcount'] > 10:
                failedstring = 'Failed Attempt'
                geolocate = DbIpCity.get(ipaddress)
                print(ipaddress, 'had', logdict['attemptcount'], 'failed login attempts'
                      ' in a 5 minute period at',
                      logdict[failedstring + str(logdict['attemptcount'])].strip(ipaddress + ','))
                print(ipaddress, 'has a Lat/Long of', geolocate.latitude, '/', geolocate.longitude)

        # Verifying number of failed attempts, starting timer for denying login
        with open('Week8Deliverables/log.json', 'r') as logfile:
            logdict = json.load(logfile)
            if logdict['attemptcount'] > 15:
                STARTTIME15ATTEMPTS.value = time.time()
                return redirect(url_for('too_many_failures'))

    # Try login again
    return render_template('loginForm.html', form=form, error_msg=error_msg)

# Page after 15 failed attempts
@APP.route('/too_many_failures')
def too_many_failures():
    """
    Page after over 15 failed attempts are made
    """
    content = '<!--Head and Title-->'
    content += '<!DOCTYPE html>'
    content += '<html><head><title>Too many failed attempts</title>'
    content += '</head><body><H1>You made over 15 failed login attempts. Try again in 5 minutes.</H1>'
    content += '<form action="/">'
    content += '<button type="submit" value="Submit">return to login page</button></form>'
    content += '</body></html>'
    return content

# Page with form to update password
@APP.route('/update_password', methods=['POST'])
def update_password():
    """
    Update password form request
    """
    form = LoginForm(request.form)
    return render_template('updatePassword.html', form=form)

# Processing new password
@APP.route('/process_update', methods=['POST'])
def process_update():
    """
    Post request for update form and validation
    """
    # Get form data
    form = LoginForm(request.form)
    if request.method == 'POST' and form.validate():
        currpassword = request.form['username']
        newpassword = request.form['password']
        curr_password_encoded = hashlib.md5(currpassword.encode()).hexdigest()
        new_password_encoded = hashlib.md5(newpassword.encode()).hexdigest()

        # Testing new password against all requirements
        with open('Week8Deliverables/savedPasswords.json', 'r+') as savedpwfile:
            savedpwdict = json.load(savedpwfile)

            # Validate old password
            if curr_password_encoded == savedpwdict['password']:

                # Validate length of new password
                if len(newpassword) >= 8:
                    if len(newpassword) <= 64:

                        # Validate complexity of new password
                        with open('Week8Deliverables/CommonPassword.txt', 'r') as commonpwfile:
                            commonpwlist = [line.rstrip('\n') for line in commonpwfile.readlines()]
                            if newpassword not in commonpwlist:
                                savedpwdict['password'] = new_password_encoded
                                savedpwfile.seek(0)
                                json.dump(savedpwdict, savedpwfile)
                                return redirect(url_for('index'))

                            # Password is in common list
                            else:
                                error_msg = "This new password is too common. Choose one that is more complex."
                    # Password is too long
                    else:
                        error_msg = "Your new password must be no more than 64 characters long. Please choose something shorter."
                # Password is too short
                else:
                    error_msg = "Your new password must be at least 8 characters long. Please choose something longer."
            # Old password is invalid
            else:
                error_msg = "Old password is incorrect. Please try again."

    # Try updating again
    return render_template('updatePassword.html', form=form, error_msg=error_msg)

# Run program
if __name__ == "__main__":
    APP.run(host='0.0.0.0', port=8080, debug=False)
