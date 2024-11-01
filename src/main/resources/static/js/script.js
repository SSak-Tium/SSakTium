
    async function signin() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const response = await fetch('/v1/auth/signin', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify({ username, password })
});

    if (response.ok) {
    // 요청이 성공하면 ssaktium/main으로 리다이렉션
    window.location.href = '/ssaktium/main';
} else {
    // 에러 처리 (예: 로그인 실패 메시지 표시)
    const errorData = await response.json();
    alert(errorData.message || '로그인 실패');
}

    return false; // 폼 제출을 방지
}

