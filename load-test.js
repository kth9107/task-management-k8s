import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
export function handleSummary(data) {
  return {
    "report.html": htmlReport(data),
    "summary.json": JSON.stringify(data, null, 2),
  };
}


export let options = {
  stages: [
    { duration: '2m', target: 100 },
    { duration: '5m', target: 100 },
    { duration: '2m', target: 200 },
    { duration: '5m', target: 200 },
    { duration: '2m', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],
    http_req_failed: ['rate<0.1'],
  },
};

export default function() {
  // GET 요청
  let response = http.get('http://127.0.0.1:8080/api/tasks');
  check(response, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });
  
  // POST 요청
  let payload = JSON.stringify({
    title: `Task ${Date.now()}`,
    description: 'Load test task',
  });
  
  let params = {
    headers: { 'Content-Type': 'application/json' },
  };
  
  response = http.post('http://127.0.0.1:8080/api/tasks', payload, params);
  check(response, {
    'task created': (r) => r.status === 201 || r.status === 200,
  });
  
  sleep(1);
}


