using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using TodoApi.Data;
using TodoApi.Models;
using Microsoft.EntityFrameworkCore;

namespace TodoApi.Controllers
{
    [Route("api/todoitems")]
    [ApiController]

    //DI , dependecy injection yapıldı. önemli kavram öğren. !!!!
    public class TodoItemsController : ControllerBase
    {
        private readonly TodoDbContext _context;

        public TodoItemsController(TodoDbContext context)
        {
            _context = context;
        }

        // POST: api/todoitems/getall
        // Tüm todo item'ların listesini döner
        [HttpPost("getall")]
        public async Task<ActionResult<IEnumerable<TodoItem>>> GetAll()
        {
            return await _context.TodoItems.ToListAsync();
        }

        // POST: api/todoitems/getbyid
        // Belirli bir id'ye sahip todo item'ı döner
        [HttpPost("getbyid")]
        public async Task<ActionResult<TodoItem>> GetById([FromBody] IdRequest request)
        {
            var item = await _context.TodoItems.FindAsync(request.Id);

            if (item == null)
            {
                return NotFound();
            }

            return item;
        }

        // POST: api/todoitems/create
        // Yeni bir todo item oluşturur
        [HttpPost("create")]
        public async Task<ActionResult<TodoItem>> Create([FromBody] TodoItem item)
        {
            _context.TodoItems.Add(item);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetById), new { id = item.Id }, item);
        }

        // POST: api/todoitems/update
        // Mevcut bir todo item'ı günceller
        [HttpPost("update")]
        public async Task<IActionResult> Update([FromBody] TodoItem item)
        {
            if (!_context.TodoItems.Any(e => e.Id == item.Id))
            {
                return NotFound();
            }

            _context.Entry(item).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                return StatusCode(StatusCodes.Status500InternalServerError, "Concurrency error");
            }

            return NoContent();
        }

        // POST: api/todoitems/delete
        // Belirli bir todo item'ı siler
        [HttpPost("delete")]
        public async Task<IActionResult> Delete([FromBody] IdRequest request)
        {
//  var item = await _context.TodoItems.FindAsync(request.Id); buurada linq mantığı.
//  kullandım ama daha katmanlı hale getirip dao ile daha farklı bir yaklaşımla yaptım.

            var item = await _context.TodoItems.FindAsync(request.Id);

            //burada dao ya gönderecektim o da doaımp'e gidecekti ara katman sql sorguları
            //linq ile gitme , başka zaman farklı bir sorgu çıkarsa hazır fonksiyonu olmaz. buna da bak . 

            if (item == null)
            {
                return NotFound();
            }

            _context.TodoItems.Remove(item);
            await _context.SaveChangesAsync();

            return NoContent();
        }
    }

    // Yardımcı Id göndermek için model
    public class IdRequest
    {
        public int Id { get; set; }
    }
}
