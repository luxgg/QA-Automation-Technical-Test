"""Local REST API for persistent POST -> GET tests. Standard library only."""
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json
from threading import Lock
records = {}
lock = Lock()
class Handler(BaseHTTPRequestHandler):
    def respond(self, status, data):
        payload = json.dumps(data).encode()
        self.send_response(status)
        self.send_header('Content-Type', 'application/json')
        self.send_header('Content-Length', str(len(payload)))
        self.end_headers()
        self.wfile.write(payload)
    def do_POST(self):
        if self.path != '/messages':
            return self.respond(404, {'error': 'not found'})
        try:
            size = int(self.headers.get('Content-Length', '0'))
            data = json.loads(self.rfile.read(size))
            if not isinstance(data, dict) or not isinstance(data.get('id'), str) or not isinstance(data.get('message'), str):
                return self.respond(400, {'error': 'id and message must be strings'})
            with lock:
                records[data['id']] = data
            return self.respond(201, data)
        except (ValueError, UnicodeError):
            return self.respond(400, {'error': 'invalid json'})
    def do_GET(self):
        if not self.path.startswith('/messages/'):
            return self.respond(404, {'error': 'not found'})
        key = self.path.split('/messages/', 1)[1]
        with lock:
            data = records.get(key)
        return self.respond(200, data) if data is not None else self.respond(404, {'error': 'not found'})
if __name__ == '__main__':
    print('Mock API listening on http://localhost:8088', flush=True)
    ThreadingHTTPServer(('127.0.0.1', 8088), Handler).serve_forever()
