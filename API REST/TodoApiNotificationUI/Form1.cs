using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Newtonsoft.Json;



namespace TodoApiNotificationUI
{
    public partial class Form1 : Form
    {

        private static readonly string BaseUrl = "http://localhost:7272/";
        private readonly HttpClient _http = new HttpClient { BaseAddress = new Uri(BaseUrl) };
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private async void btnRefresh_Click(object sender, EventArgs e)
        {

            try
            {
                btnRefresh.Enabled = false;

                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:7272/"); // API adresin
                    HttpResponseMessage resp = await client.GetAsync("api/FirebaseTokens");

                    resp.EnsureSuccessStatusCode();

                    string json = await resp.Content.ReadAsStringAsync();
                    var list = Newtonsoft.Json.JsonConvert.DeserializeObject<List<FirebaseToken>>(json);

                    dgvTokens.DataSource = list;
                    if (dgvTokens.Columns["Token"] != null)
                        dgvTokens.Columns["Token"].Visible = false; // uzun olduğu için gizli tut
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Liste alınamadı: " + ex.Message);
            }
            finally
            {
                btnRefresh.Enabled = true;
            }

        }

        private async void btnSend_Click(object sender, EventArgs e)
        {
            if (dgvTokens.SelectedRows.Count == 0)
            {
                MessageBox.Show("Lütfen bir cihaz seçin.");
                return;
            }

            string token = dgvTokens.SelectedRows[0].Cells["Token"].Value?.ToString();

            if (string.IsNullOrEmpty(token))
            {
                MessageBox.Show("Token bulunamadı.");
                return;
            }

            string title = txtTitle.Text.Trim();
            string message = txtBody.Text.Trim();

            if (string.IsNullOrEmpty(title) || string.IsNullOrEmpty(message))
            {
                MessageBox.Show("Başlık ve mesaj boş olamaz.");
                return;
            }

            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:7272/"); // Web API adresin

                var payload = new
                {
                    title = title,
                    body = message,
                    deviceTokens = new List<string> { token }
                };

                var json = Newtonsoft.Json.JsonConvert.SerializeObject(payload);

                var content = new StringContent(json, Encoding.UTF8, "application/json");

                HttpResponseMessage response = await client.PostAsync("api/notifications/send", content);

                if (response.IsSuccessStatusCode)
                {
                    MessageBox.Show("Bildirim başarıyla gönderildi.");
                }
                else
                {
                    MessageBox.Show($"Hata: {response.StatusCode}");
                }
            }
        }
    }

    public class FirebaseToken
    {
        public int Id { get; set; }
        public string Token { get; set; }
        public string DeviceModel { get; set; }
        public string Manufacturer { get; set; }
        public string Platform { get; set; }
        public string AppVersion { get; set; }
        public DateTime CreatedAt { get; set; }
    }

}
