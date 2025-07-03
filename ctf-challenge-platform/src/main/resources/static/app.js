const API_BASE = "http://localhost:8080/api";

// Carrega desafios no painel
async function loadChallenges() {
  const res = await fetch(API_BASE + "/challenges");
  const list = await res.json();
  const grid = document.getElementById("challenges-list");
  grid.innerHTML = "";
  for (const c of list) {
    grid.innerHTML += `
      <div class="card">
        <div class="title">${escapeHtml(c.title)}</div>
        <div class="desc">${escapeHtml(c.description)}</div>
        <div class="meta">Category: <b>${escapeHtml(c.category)}</b></div>
        <div class="meta">Difficulty: <b>${escapeHtml(c.difficulty)}</b></div>
        <button class="btn" onclick="openModal(${c.id}, '${escapeHtml(c.title)}')">Submit Flag</button>
      </div>
    `;
  }
}

// Carrega scoreboard
async function loadScoreboard() {
  const res = await fetch(API_BASE + "/challenges/scoreboard");
  const data = await res.json();
  const tb = document.querySelector("#scoreboard-table tbody");
  tb.innerHTML = "";
  for (const row of data) {
    const date = new Date(Number(row.timestamp));
    tb.innerHTML += `
      <tr>
        <td>${escapeHtml(row.user)}</td>
        <td>${escapeHtml(row.challenge)}</td>
        <td>${date.toLocaleString()}</td>
      </tr>
    `;
  }
}

// Modal lógica
let modalChallengeId = null;
function openModal(id, title) {
  modalChallengeId = id;
  document.getElementById("modal-title").textContent = `Submit Flag — ${title}`;
  document.getElementById("modal-flag").value = "";
  document.getElementById("modal-username").value = "";
  document.getElementById("flag-result").innerHTML = "";
  document.getElementById("flag-modal").classList.remove("hidden");
  document.getElementById("flag-modal").classList.add("show");
}
function closeModal() {
  document.getElementById("flag-modal").classList.remove("show");
  document.getElementById("flag-modal").classList.add("hidden");
}
window.closeModal = closeModal;

// Submit flag handler
document.getElementById("flag-form").onsubmit = async (e) => {
  e.preventDefault();
  const username = document.getElementById("modal-username").value.trim();
  const flag = document.getElementById("modal-flag").value.trim();
  if (!username || !flag) return;
  const res = await fetch(`${API_BASE}/challenges/${modalChallengeId}/submit?username=${encodeURIComponent(username)}&flag=${encodeURIComponent(flag)}`, {
    method: "POST"
  });
  const data = await res.json();
  document.getElementById("flag-result").innerHTML = data.message || (data.correct ? "✅ Flag correta!" : "❌ Flag incorreta.");
  if (data.correct) {
    await loadScoreboard();
  }
};

// Helper pra evitar XSS
function escapeHtml(str) {
  if (!str) return "";
  return str.replace(/[&<>"']/g, function (m) {
    return ({
      '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    })[m];
  });
}

window.onload = () => {
  loadChallenges();
  loadScoreboard();
};
