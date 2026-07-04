import request from '@/utils/request'

export function chat(data) {
  return request({
    url: '/learn-agent/a/agent/chat',
    method: 'post',
    data,
    timeout: 120000
  })
}

export function getPreference() {
  return request({
    url: '/learn-agent/a/agent/preference',
    method: 'get'
  })
}

export function updatePreference(data) {
  return request({
    url: '/learn-agent/a/agent/preference',
    method: 'put',
    data
  })
}
