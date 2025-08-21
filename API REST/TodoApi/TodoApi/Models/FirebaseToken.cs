using System;
using System.ComponentModel.DataAnnotations;

namespace TodoApi.Models
{
    public class FirebaseToken
    {
        [Key]
        public int Id { get; set; }

        [Required]
        public string Token { get; set; }

        public string DeviceModel { get; set; }

        public string Manufacturer { get; set; }

        public string Platform { get; set; }

        public string AppVersion { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
