const ArcadeUi = (() => {
    const money = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD"
    });

    const dateTime = new Intl.DateTimeFormat("en-US", {
        dateStyle: "short",
        timeStyle: "short"
    });

    const $ = (selector) => document.querySelector(selector);

    function readForm(form) {
        return Object.fromEntries(new FormData(form).entries());
    }

    function resetForm(form) {
        form.reset();
    }

    function setLoading(isLoading) {
        document.body.classList.toggle("is-loading", isLoading);
        document.body.setAttribute("aria-busy", String(isLoading));

        const button = $("#refreshButton");
        button.disabled = isLoading;
        button.textContent = isLoading ? "Refreshing..." : "Refresh workspace";

        if (isLoading) {
            setConnectionState("syncing");
        }
    }

    function setConnectionState(status) {
        const element = $("#syncStatus");
        const labelByStatus = {
            ready: "Ready",
            syncing: "Syncing",
            error: "Offline"
        };

        element.textContent = labelByStatus[status] || "Ready";
        element.className = `status-pill ${status}`;
    }

    function toast(message, type = "success") {
        const element = $("#toast");
        element.textContent = message;
        element.className = `toast visible ${type}`;

        window.clearTimeout(toast.timeout);
        toast.timeout = window.setTimeout(() => {
            element.classList.remove("visible");
        }, 3600);
    }

    function renderDashboard(state) {
        renderSummary(state.report);
        renderCustomers(state.customers);
        renderCategories(state.categories);
        renderProducts(state.products);
        renderSaleControls(state.customers, state.products);
        renderSaleItems(state.saleItems, state.products);
        renderSales(state.sales);
    }

    function renderSummary(report) {
        $("#totalSales").textContent = report.salesCount;
        $("#totalItems").textContent = report.itemsSold;
        $("#totalRevenue").textContent = money.format(report.totalRevenue);
    }

    function renderCustomers(customers) {
        const list = $("#customersList");
        clear(list);

        if (customers.length === 0) {
            list.append(emptyState("No customers registered yet."));
            return;
        }

        customers.forEach((customer) => {
            const item = createElement("article", "item");
            const content = createElement("div");
            content.append(textElement("strong", customer.name));
            content.append(textElement("span", customer.cpf));
            item.append(content);
            item.append(textElement("span", customer.email || customer.phone || "-"));
            list.append(item);
        });
    }

    function renderCategories(categories) {
        const list = $("#categoriesList");
        clear(list);

        if (categories.length === 0) {
            list.append(emptyState("No categories registered yet."));
        } else {
            categories.forEach((category) => {
                list.append(textElement("span", category.name, "chip"));
            });
        }

        fillSelect($("#productForm select[name='categoryId']"), categories, "Select category", (category) => category.name);
    }

    function renderProducts(products) {
        const grid = $("#productsGrid");
        clear(grid);

        if (products.length === 0) {
            grid.append(emptyState("No products registered yet."));
            return;
        }

        products.forEach((product) => {
            grid.append(productCard(product));
        });
    }

    function renderSaleControls(customers, products) {
        fillSelect($("#saleItemForm select[name='customerId']"), customers, "Select customer", (customer) => customer.name);
        fillSelect(
            $("#saleItemForm select[name='productId']"),
            products,
            "Select product",
            (product) => `${product.name} - ${money.format(product.price)} (${product.stockQuantity} in stock)`,
            (option, product) => {
                option.disabled = product.stockQuantity <= 0;
            }
        );
    }

    function renderSaleItems(saleItems, products) {
        const list = $("#saleItems");
        clear(list);

        if (saleItems.length === 0) {
            list.append(emptyState("No items added to the current sale."));
            $("#saleDraftTotal").textContent = money.format(0);
            return;
        }

        let total = 0;
        saleItems.forEach((item, index) => {
            const product = products.find((currentProduct) => currentProduct.id === item.productId);
            const subtotal = (product?.price || 0) * item.quantity;
            total += subtotal;

            const article = createElement("article", "item sale-item");
            const content = createElement("div");
            content.append(textElement("strong", product?.name || "Product"));
            content.append(textElement("span", `Quantity: ${item.quantity} | Subtotal: ${money.format(subtotal)}`));

            const button = createElement("button", "quiet-button");
            button.type = "button";
            button.dataset.removeItem = String(index);
            button.textContent = "Remove";

            article.append(content);
            article.append(button);
            list.append(article);
        });

        $("#saleDraftTotal").textContent = money.format(total);
    }

    function renderSales(sales) {
        const list = $("#salesList");
        clear(list);

        if (sales.length === 0) {
            list.append(emptyState("No sales registered yet."));
            return;
        }

        sales.slice().reverse().forEach((sale) => {
            const item = createElement("article", "item");
            const content = createElement("div");
            content.append(textElement("strong", `#${sale.id} - ${sale.customer.name}`));
            content.append(textElement("span", dateTime.format(new Date(sale.saleDateTime))));
            item.append(content);
            item.append(textElement("span", money.format(sale.total), "amount"));
            list.append(item);
        });
    }

    function updateActionStates(state) {
        const hasCategories = state.categories.length > 0;
        const hasCustomers = state.customers.length > 0;
        const hasAvailableProducts = state.products.some((product) => product.stockQuantity > 0);

        $("#productForm button[type='submit']").disabled = !hasCategories;
        $("#saleItemForm button[type='submit']").disabled = !hasCustomers || !hasAvailableProducts;
        $("#finishSaleButton").disabled = state.saleItems.length === 0 || !hasCustomers;
    }

    function fillSelect(select, items, placeholder, label, customizeOption) {
        clear(select);

        const initial = document.createElement("option");
        initial.value = "";
        initial.textContent = placeholder;
        initial.disabled = true;
        initial.selected = true;
        select.append(initial);

        items.forEach((item) => {
            const option = document.createElement("option");
            option.value = String(item.id);
            option.textContent = label(item);
            customizeOption?.(option, item);
            select.append(option);
        });
    }

    function productCard(product) {
        const card = createElement("article", "product-card");
        const header = createElement("div", "product-card-header");
        const titleGroup = createElement("div", "product-title-group");
        const badges = createElement("div", "product-badges");
        const footer = createElement("div", "product-card-footer");

        titleGroup.append(textElement("h2", product.name));
        titleGroup.append(textElement("p", product.description || "No description registered.", "product-description"));

        badges.append(textElement("span", product.category.name, "badge category-badge"));
        badges.append(stockBadge(product.stockQuantity));

        header.append(titleGroup, badges);
        footer.append(textElement("strong", money.format(product.price), "product-price"));
        footer.append(stockActions(product));
        card.append(header, footer);

        return card;
    }

    function stockBadge(quantity) {
        const className = quantity <= 3 ? "badge stock-badge low" : "badge stock-badge";
        const text = quantity <= 3 ? `${quantity} low stock` : `${quantity} in stock`;
        return textElement("span", text, className);
    }

    function stockActions(product) {
        const controls = createElement("div", "stock-controls");
        const input = document.createElement("input");
        input.type = "number";
        input.min = "1";
        input.step = "1";
        input.value = "1";
        input.inputMode = "numeric";
        input.setAttribute("aria-label", `Stock quantity for ${product.name}`);

        const addButton = stockButton("+", "in", product.id, `Add stock to ${product.name}`);
        const removeButton = stockButton("-", "out", product.id, `Remove stock from ${product.name}`);

        controls.append(input, addButton, removeButton);
        return controls;
    }

    function stockButton(label, action, productId, title) {
        const button = createElement("button", "icon-button");
        button.type = "button";
        button.textContent = label;
        button.dataset.stockAction = action;
        button.dataset.productId = String(productId);
        button.title = title;
        button.setAttribute("aria-label", title);
        return button;
    }

    function emptyState(message) {
        return textElement("p", message, "empty-state");
    }

    function textElement(tag, text, className) {
        const element = createElement(tag, className);
        element.textContent = text;
        return element;
    }

    function createElement(tag, className) {
        const element = document.createElement(tag);
        if (className) {
            element.className = className;
        }
        return element;
    }

    function clear(element) {
        element.replaceChildren();
    }

    return {
        $,
        money,
        readForm,
        resetForm,
        setLoading,
        setConnectionState,
        toast,
        renderDashboard,
        renderSaleItems,
        updateActionStates
    };
})();
