using Google.Apis.Auth.OAuth2;
using Newtonsoft.Json;
using System.Net.Http.Headers;
using System.Text;

namespace TodoApi.Services
{
    public class FirebaseNotificationService
    {
        private readonly string _projectId = "todoappbase-73b86"; // JSON içindeki project_id
        private readonly string _serviceAccountFile = "serviceAccountKey.json"; // JSON dosyasının API projesindeki adı

        public async Task SendNotificationAsync(string title, string body, List<string> deviceTokens)
        {
            // Access Token al
            var accessToken = await GetAccessTokenAsync();

            var url = $"https://fcm.googleapis.com/v1/projects/{_projectId}/messages:send";

            using var httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", accessToken);

            foreach (var token in deviceTokens)
            {
                var message = new
                {
                    message = new
                    {
                        token = token,
                        notification = new
                        {
                            title = title,
                            body = body
                        }
                    }
                };

                var json = JsonConvert.SerializeObject(message);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var response = await httpClient.PostAsync(url, content);

                if (!response.IsSuccessStatusCode)
                {
                    var error = await response.Content.ReadAsStringAsync();
                    throw new Exception($"Firebase bildirimi başarısız: {response.StatusCode} - {error}");
                }
            }
        }

        private async Task<string> GetAccessTokenAsync()
        {
            GoogleCredential credential;
            using (var stream = new FileStream(_serviceAccountFile, FileMode.Open, FileAccess.Read))
            {
                credential = GoogleCredential.FromStream(stream)
                    .CreateScoped("https://www.googleapis.com/auth/firebase.messaging");
            }

            var token = await credential.UnderlyingCredential.GetAccessTokenForRequestAsync();
            return token;
        }
    }
}
