#!.venv/bin/python
from flask import Flask, request, abort

app = Flask(__name__)

clipboard = 'contents'

@app.route('/clipboard', methods=['GET'])
def get_clipboard():
    return clipboard

@app.route('/clipboard', methods=['POST'])
def update_clipboard():
    if not request.json or not 'contents' in request.json:
        abort(400)
    global clipboard
    clipboard = request.json['contents']
    return clipboard, 201

if __name__ == '__main__':
    app.run('0.0.0.0', 5000, debug=True)
