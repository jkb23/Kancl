let fetch;
let serverInstance;

async function startProxyServer() {
    fetch = await import('node-fetch').then(module => module.default);

    const express = require('express');
    const app = express();
    const PORT = 8082;
    const cors = require('cors');

    app.use(cors());
    app.use(express.json());

    app.use('/', async (request, response) => {
        const url = request.query.url;
        if (!url) {
            return response.status(400).json({error: 'Missing URL parameter.'});
        }

        const forwardedHeaders = {...request.headers};
        delete forwardedHeaders.host;
        delete forwardedHeaders.origin;
        delete forwardedHeaders.referer;

        try {
            const response = await fetch(url, {
                method: request.method,
                headers: forwardedHeaders,
                body: request.method !== 'GET' && request.method !== 'HEAD' ? JSON.stringify(request.body) : undefined,
            });

            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                const data = await response.json();
                response.header("Access-Control-Allow-Origin", "*");
                response.status(response.status).json(data);
            } else {
                const text = await response.text();
                response.header("Access-Control-Allow-Origin", "*");
                response.status(response.status).send(text);
            }

        } catch (error) {
            console.error('Proxy error:', error);
            response.status(500).json({error: 'Proxy error.', details: error.message});
        }
    });

    serverInstance = app.listen(PORT, () => {
        console.log(`Proxy server is running on port ${PORT}`);
    });
}

function stopProxyServer() {
    if (serverInstance) {
        serverInstance.close(() => {
            console.log(`Proxy server stopped.`);
        });
    }
}

startProxyServer();

process.on('SIGINT', stopProxyServer);
process.on('SIGTERM', stopProxyServer);
