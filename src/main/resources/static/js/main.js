/* =============================================
   ShopWave — Main JavaScript
   ============================================= */

// =============================================
// Mobile Menu Toggle
// =============================================
function toggleMobileMenu() {
    const menu = document.getElementById('mobileMenu');
    if (menu) {
        menu.classList.toggle('open');
    }
}

// =============================================
// Close Mobile Menu on Outside Click
// =============================================
document.addEventListener('click', function(e) {
    const menu = document.getElementById('mobileMenu');
    const toggle = document.querySelector('.mobile-toggle');

    if (menu && toggle && !menu.contains(e.target) && !toggle.contains(e.target)) {
        menu.classList.remove('open');
    }
});

// =============================================
// DOM Ready Logic
// =============================================
document.addEventListener('DOMContentLoaded', function() {
    console.log("main.js loaded");

    // =============================================
    // Star Rating Renderer
    // =============================================
    document.querySelectorAll('.stars[data-rating]').forEach(function(el) {
        const rating = parseFloat(el.getAttribute('data-rating')) || 0;
        const full = Math.floor(rating);
        const half = rating % 1 >= 0.5 ? 1 : 0;
        const empty = 5 - full - half;

        el.innerHTML =
            '★'.repeat(full) +
            (half ? '½' : '') +
            '☆'.repeat(empty);

        el.style.color = '#fbbf24';
    });

    // =============================================
    // Auto-dismiss Flash Messages
    // =============================================
    const flashes = document.querySelectorAll('.flash');
    flashes.forEach(function(flash) {
        setTimeout(function() {
            flash.style.transition = 'opacity .4s ease';
            flash.style.opacity = '0';

            setTimeout(function() {
                flash.remove();
            }, 400);
        }, 4000);
    });

    // =============================================
    // Image Fallback
    // =============================================
    document.querySelectorAll('img[onerror]').forEach(function(img) {
        if (!img.getAttribute('src') || img.getAttribute('src') === window.location.href) {
            img.src = 'https://via.placeholder.com/300x300?text=No+Image';
        }
    });

    // =============================================
    // Smooth Add-to-Cart Feedback
    // =============================================
    document.querySelectorAll('form[action*="/cart/add"]').forEach(function(form) {
        form.addEventListener('submit', function() {
            const btn = form.querySelector('button[type="submit"]');
            if (btn) {
                btn.innerHTML = '<i class="fas fa-check"></i>';
                btn.style.background = '#1a9e5c';
            }
        });
    });

    // =============================================
    // Confirm Delete
    // =============================================
    document.querySelectorAll('form[data-confirm]').forEach(function(form) {
        form.addEventListener('submit', function(e) {
            if (!confirm(form.dataset.confirm)) {
                e.preventDefault();
            }
        });
    });

    // =============================================
    // FAQ Toggle
    // =============================================
    document.querySelectorAll('.faq-question').forEach(function(button) {
        button.addEventListener('click', function() {
            const item = button.closest('.faq-item');
            if (item) {
                item.classList.toggle('open');
            }
        });
    });

    // =============================================
    // Scroll Reveal Animation
    // =============================================
    if ('IntersectionObserver' in window) {
        const observer = new IntersectionObserver(function(entries) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.1 });

        document.querySelectorAll('.product-card, .category-card, .stat-card').forEach(function(el) {
            el.style.opacity = '0';
            el.style.transform = 'translateY(24px)';
            el.style.transition = 'opacity .4s ease, transform .4s ease';
            observer.observe(el);
        });
    } else {
        document.querySelectorAll('.product-card, .category-card, .stat-card').forEach(function(el) {
            el.style.opacity = '1';
            el.style.transform = 'translateY(0)';
        });
    }

    // =============================================
    // PAYMENT LOGIC
    // =============================================

    // -----------------------------
    // Payment Screen Switch Helper
    // -----------------------------
    function switchPaymentScreen(type) {
        document.querySelectorAll('.payment-screen').forEach(function(screen) {
            screen.classList.remove('active');
        });

        const target = document.getElementById('screen-' + type);
        if (target) {
            target.classList.add('active');
        }

        console.log("Selected payment method:", type);
    }

    // =============================================
    // Payment Method Selector (robust fix)
    // =============================================
    const paymentOptions = document.querySelectorAll('.payment-option[data-payment]');
    const paymentRadios = document.querySelectorAll('input[name="method"]');

    // Clicking the whole payment option
    paymentOptions.forEach(function(option) {
        option.addEventListener('click', function() {
            const type = option.dataset.payment;
            const radio = option.querySelector('input[type="radio"]');

            if (radio) {
                radio.checked = true;
                radio.dispatchEvent(new Event('change', { bubbles: true }));
            } else {
                switchPaymentScreen(type);
            }
        });
    });

    // Changing actual radio input
    paymentRadios.forEach(function(radio) {
        radio.addEventListener('change', function() {
            const selectedOption = radio.closest('.payment-option');
            if (selectedOption) {
                const type = selectedOption.dataset.payment;
                switchPaymentScreen(type);
            }
        });
    });

    // Ensure correct screen on page load
    const checkedPayment = document.querySelector('input[name="method"]:checked');
    if (checkedPayment) {
        const selectedOption = checkedPayment.closest('.payment-option');
        if (selectedOption) {
            switchPaymentScreen(selectedOption.dataset.payment);
        }
    }

    // =============================================
    // UPI App Selection
    // =============================================
    let selectedUpiApp = '';

    document.querySelectorAll('.upi-app[data-upi]').forEach(function(app) {
        app.addEventListener('click', function() {
            document.querySelectorAll('.upi-app').forEach(function(a) {
                a.classList.remove('selected');
            });

            app.classList.add('selected');
            selectedUpiApp = app.dataset.upi || '';
            console.log("Selected UPI app:", selectedUpiApp);
        });
    });

    // =============================================
    // UPI Payment Simulation
    // =============================================
    const upiPayBtn = document.getElementById('upiPayBtn');
    if (upiPayBtn) {
        upiPayBtn.addEventListener('click', function() {
            const upiId = document.getElementById('upiId')?.value.trim();
            const upiForm = document.getElementById('upiForm');

            if (!upiId) {
                alert('Please enter your UPI ID');
                return;
            }

            // basic UPI format validation
            const upiRegex = /^[a-zA-Z0-9.\-_]{2,}@[a-zA-Z]{2,}$/;
            if (!upiRegex.test(upiId)) {
                alert('Please enter a valid UPI ID (example: yourname@upi)');
                return;
            }

            if (!selectedUpiApp) {
                alert('Please select a UPI app');
                return;
            }

            upiPayBtn.disabled = true;
            upiPayBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';

            showProcessing('Connecting to ' + selectedUpiApp + '...', 'Waiting for payment confirmation');

            setTimeout(function() {
                updateProcessing('✅ Payment Successful!', 'Redirecting to your order...');

                setTimeout(function() {
                    if (upiForm) {
                        upiForm.submit();
                    }
                }, 1500);
            }, 2500);
        });
    }

    // =============================================
    // Card Payment Simulation
    // =============================================
    const cardPayBtn = document.getElementById('cardPayBtn');
    if (cardPayBtn) {
        cardPayBtn.addEventListener('click', function() {
            const num = document.getElementById('cardNumber')?.value.trim() || '';
            const name = document.getElementById('cardName')?.value.trim() || '';
            const exp = document.getElementById('cardExpiry')?.value.trim() || '';
            const cvv = document.getElementById('cardCvv')?.value.trim() || '';
            const cardForm = document.getElementById('cardForm');

            const cleanNum = num.replace(/\s/g, '');

            if (!num || !name || !exp || !cvv) {
                alert('Please fill all card details');
                return;
            }

            if (!/^\d{16}$/.test(cleanNum)) {
                alert('Please enter a valid 16-digit card number');
                return;
            }

            if (!/^[A-Za-z\s]{2,}$/.test(name)) {
                alert('Please enter a valid cardholder name');
                return;
            }

            if (!/^\d{2}\/\d{2}$/.test(exp)) {
                alert('Please enter expiry date in MM/YY format');
                return;
            }

            const [month, year] = exp.split('/').map(Number);
            if (month < 1 || month > 12) {
                alert('Please enter a valid expiry month');
                return;
            }

            if (!/^\d{3}$/.test(cvv)) {
                alert('Please enter a valid 3-digit CVV');
                return;
            }

            cardPayBtn.disabled = true;
            cardPayBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';

            showProcessing('Verifying Card...', 'Contacting your bank');

            setTimeout(function() {
                updateProcessing('🔐 Authenticating...', 'Please wait');

                setTimeout(function() {
                    updateProcessing('✅ Payment Successful!', 'Redirecting to your order...');

                    setTimeout(function() {
                        if (cardForm) {
                            cardForm.submit();
                        }
                    }, 1500);
                }, 1500);
            }, 2000);
        });
    }

    // =============================================
    // Card Number Formatting
    // =============================================
    const cardNumber = document.getElementById('cardNumber');
    if (cardNumber) {
        cardNumber.addEventListener('input', function() {
            let value = cardNumber.value.replace(/\D/g, '').substring(0, 16);
            cardNumber.value = value.replace(/(.{4})/g, '$1 ').trim();
        });
    }

    // =============================================
    // Card Expiry Formatting
    // =============================================
    const cardExpiry = document.getElementById('cardExpiry');
    if (cardExpiry) {
        cardExpiry.addEventListener('input', function() {
            let value = cardExpiry.value.replace(/\D/g, '').substring(0, 4);

            if (value.length >= 2) {
                value = value.substring(0, 2) + '/' + value.substring(2);
            }

            cardExpiry.value = value;
        });
    }

    // =============================================
    // CVV Only Digits
    // =============================================
    const cardCvv = document.getElementById('cardCvv');
    if (cardCvv) {
        cardCvv.addEventListener('input', function() {
            cardCvv.value = cardCvv.value.replace(/\D/g, '').substring(0, 3);
        });
    }

});

// =============================================
// Admin: Preview Category Image
// =============================================
function previewCategoryImage(url) {
    const img = document.getElementById('catImgPreview');
    if (img) {
        img.src = url;
        img.style.display = url ? 'block' : 'none';
    }
}

// =============================================
// Processing Overlay Helpers
// =============================================
function showProcessing(title, msg) {
    const titleEl = document.getElementById('processingTitle');
    const msgEl = document.getElementById('processingMsg');
    const overlay = document.getElementById('processingOverlay');

    if (titleEl) titleEl.textContent = title;
    if (msgEl) msgEl.textContent = msg;
    if (overlay) overlay.classList.add('show');
}

function updateProcessing(title, msg) {
    const titleEl = document.getElementById('processingTitle');
    const msgEl = document.getElementById('processingMsg');

    if (titleEl) titleEl.textContent = title;
    if (msgEl) msgEl.textContent = msg;
}

// ✅ NEW — changes value AND auto-submits the form
function updateQty(itemId, delta) {
    const input = document.getElementById('qty-' + itemId);
    const newVal = Math.max(1, parseInt(input.value) + delta);
    input.value = newVal;
    // Auto submit the parent form
    input.closest('form').submit();
}