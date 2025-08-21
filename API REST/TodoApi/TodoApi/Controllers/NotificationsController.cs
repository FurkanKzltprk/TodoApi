using Microsoft.AspNetCore.Mvc;
using TodoApi.Services;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace TodoApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class NotificationsController : ControllerBase
    {
        private readonly FirebaseNotificationService _firebaseService;

        public NotificationsController(FirebaseNotificationService firebaseService)
        {
            _firebaseService = firebaseService;
        }

        [HttpPost("send")]
        public async Task<IActionResult> Send([FromBody] NotificationRequest request)
        {
            if (request.DeviceTokens == null || request.DeviceTokens.Count == 0)
                return BadRequest("En az bir token gerekli.");

            await _firebaseService.SendNotificationAsync(
                request.Title,  
                request.Body,
                request.DeviceTokens
            );

            return Ok(new { success = true });
        }
    }

    public class NotificationRequest
    {
        public string Title { get; set; }
        public string Body { get; set; }
        public List<string> DeviceTokens { get; set; }
    }
}
