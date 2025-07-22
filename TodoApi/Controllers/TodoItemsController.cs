using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using TodoApi.Data;
using TodoApi.Models;  //DbCoontext >> data klasörü içerisindeki !!s
using Microsoft.EntityFrameworkCore;

namespace TodoApi.Controllers
{




    [Route("api/[controller]")]
    [ApiController]



    public class TodoItemsController : ControllerBase
    {

        //Dependency injection dediğimiz şey burada yapıldı.
        private readonly TodoDbContext _context;
        public TodoItemsController(TodoDbContext context)
        {
            _context = context;
        }

        // ↓↓ EndPointler buraya gelecek ↓↓

        //get metodu

        [HttpGet]

        public async Task<ActionResult<IEnumerable<TodoItem>>> GetTodoItems()
        {
            return await _context.TodoItems.ToListAsync();
        }


        //HTTP POST isteği.

        [HttpPost]
        public async Task<ActionResult<TodoItem>> PostTodoItem(TodoItem item)
        {
            _context.TodoItems.Add(item);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetTodoItems), new { id = item.Id }, item);
        }

        //HTTP Get id

        [HttpGet("{id}")]

        public async Task<ActionResult<TodoItem>> GetToDoItem(int id)
        {

            var item = await _context.TodoItems.FindAsync(id);

            if (item == null)
            {
                return NotFound();
            }

            return item;

        }

        [HttpPut("{id}")]

        public async Task <ActionResult<TodoItem>> PutTodoItem(int id ,TodoItem item)
        {
            if ( id != item.Id)
            {
                return BadRequest();

            }

            _context.Entry(item).State = EntityState.Modified;


            try
            {
                await _context.SaveChangesAsync();


            }
            catch (DbUpdateConcurrencyException)
            {
                if(!_context.TodoItems.Any(e => e.Id == id))
                {
                    return NotFound();
                }else
                {
                    throw;
                }

                
            }


            return NoContent();

        }

        [HttpDelete("{id}")]

        public async Task<IActionResult> DeleteTodoItem(int id)
        {
            var item = await _context.TodoItems.FindAsync(id);

            if (item == null)
            {
                return NotFound();
            }

            _context.TodoItems.Remove(item);
            await _context.SaveChangesAsync();

            return NoContent();
        }

         
    }
}
