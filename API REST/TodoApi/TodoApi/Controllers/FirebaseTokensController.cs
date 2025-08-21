using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TodoApi.Data;
using TodoApi.Models;
using TodoApi.Services;

namespace TodoApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FirebaseTokensController : ControllerBase
    {
        private readonly TodoDbContext _context;

        public FirebaseTokensController(TodoDbContext context)
        {
            _context = context;
        }

        // POST: api/FirebaseTokens
        [HttpPost]
        public async Task<IActionResult> PostToken([FromBody] FirebaseToken token)
        {
            if (string.IsNullOrWhiteSpace(token.Token))
            {
                return BadRequest("Token is required.");
            }

            //aynı token var mı için kontrol ekledim.
            bool ayniTokenVarMi = await _context.FirebaseTokens.AnyAsync(t => t.Token == token.Token);
            if (ayniTokenVarMi)
            {
                return Ok(new { succes = false, message = "Token zaten var " });
            }

            token.CreatedAt = DateTime.UtcNow;

            _context.FirebaseTokens.Add(token);
            await _context.SaveChangesAsync();

            return Ok(new { success = true, message = "Token saved successfully !!." });



        }

        // GET: api/FirebaseTokens
        [HttpGet]
        public IActionResult GetAllTokens()
        {
            var tokens = _context.FirebaseTokens.ToList();
            return Ok(tokens);
        }



        [HttpPost("send")]
        public async Task<IActionResult> SendNotification([FromBody] FirebaseNotificationRequest request, [FromServices] FirebaseNotificationService notificationService)
        {
            if (string.IsNullOrWhiteSpace(request.Title) || string.IsNullOrWhiteSpace(request.Body))
                return BadRequest("Başlık ve mesaj zorunludur.");

            var tokens = _context.FirebaseTokens
                .Where(t => request.DeviceIds.Contains(t.Id))
                .Select(t => t.Token)
                .ToList();

            if (!tokens.Any())
                return NotFound("Seçilen cihazlara ait token bulunamadı.");

            await notificationService.SendNotificationAsync(request.Title, request.Body, tokens);
            return Ok(new { success = true, message = "Bildirim gönderildi." });
        }

        public class FirebaseNotificationRequest
        {
            public string Title { get; set; }
            public string Body { get; set; }
            public List<int> DeviceIds { get; set; } // Seçilen cihazların Id'si
        }

    }
}
