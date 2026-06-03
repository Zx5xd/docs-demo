const TOKEN_KEYS = {
    access: 'access_token',
    refresh: 'refresh_token',
};

function saveTokens(loginResponse) {
    if (loginResponse.access_token) {
        localStorage.setItem(TOKEN_KEYS.access, loginResponse.access_token);
    }
    if (loginResponse.refresh_token) {
        localStorage.setItem(TOKEN_KEYS.refresh, loginResponse.refresh_token);
    }
}

function getAccessToken() {
    return localStorage.getItem(TOKEN_KEYS.access);
}

function clearTokens() {
    localStorage.removeItem(TOKEN_KEYS.access);
    localStorage.removeItem(TOKEN_KEYS.refresh);
}

async function apiRequest(url, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {}),
    };

    const response = await fetch(url, {
        ...options,
        headers,
    });

    let body = null;
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        body = await response.json();
    }

    if (!response.ok) {
        const message = body?.message || `요청에 실패했습니다. (${response.status})`;
        throw new Error(message);
    }

    return body;
}

function showBanner(elementId, message, type = 'error') {
    const banner = document.getElementById(elementId);
    if (!banner) {
        return;
    }
    banner.textContent = message;
    banner.className = `message-banner visible ${type}`;
}

function hideBanner(elementId) {
    const banner = document.getElementById(elementId);
    if (!banner) {
        return;
    }
    banner.className = 'message-banner';
    banner.textContent = '';
}

function getQueryMessage() {
    const params = new URLSearchParams(window.location.search);
    return params.get('message');
}
