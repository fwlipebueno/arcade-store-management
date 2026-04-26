const ArcadeApi = (() => {
    const API_URL = "http://localhost:8080/api";

    async function request(path, options = {}) {
        const config = {
            method: options.method || "GET",
            headers: {
                ...(options.body ? { "Content-Type": "application/json" } : {}),
                ...(options.headers || {})
            },
            body: options.body ? JSON.stringify(options.body) : undefined
        };

        let response;
        try {
            response = await fetch(`${API_URL}${path}`, config);
        } catch {
            throw new Error("Could not reach the backend. Check if Spring Boot is running.");
        }

        if (response.status === 204) {
            return null;
        }

        const body = await parseBody(response);

        if (!response.ok) {
            const fields = body?.fields ? Object.values(body.fields).join(" ") : "";
            const message = body?.message || "The operation could not be completed.";
            throw new Error([message, fields].filter(Boolean).join(" "));
        }

        return body;
    }

    async function parseBody(response) {
        const text = await response.text();
        if (!text) {
            return null;
        }

        try {
            return JSON.parse(text);
        } catch {
            return null;
        }
    }

    async function loadDashboard() {
        const [customers, categories, products, sales, report] = await Promise.all([
            request("/customers"),
            request("/categories"),
            request("/products"),
            request("/sales"),
            request("/sales/report")
        ]);

        return { customers, categories, products, sales, report };
    }

    return {
        loadDashboard,
        createCustomer: (data) => request("/customers", { method: "POST", body: data }),
        createCategory: (data) => request("/categories", { method: "POST", body: data }),
        createProduct: (data) => request("/products", { method: "POST", body: data }),
        changeStock: (productId, action, quantity) => request(`/products/${productId}/stock/${action}`, {
            method: "POST",
            body: { quantity }
        }),
        registerSale: (data) => request("/sales", { method: "POST", body: data })
    };
})();
