const { EventEmitter } = require('events');
const assert = require('assert');

class PromiseQueue extends EventEmitter {
  constructor (limit) {
    super();
    this.limit = limit;
    this.queue = []; // promise generator funcs
    this.done = false;
    this.workers = new Set();
    this._promise = new Promise((resolve, reject) => {
      this._resolve = resolve;
      this._reject = reject;
    });
  }

  _add (func, push = true) {
    assert(!this.done, 'This Queue instance has terminated.');
    if (push) {
      this.queue.push(func);
    } else {
      this.queue.unshift(func);
    }

    if (this.workers.size < this.limit) {
      let worker = new Worker(this);
      this.workers.add(worker);

      worker.on('terminate', () => {
        this.workers.delete(worker);
        if (this.workers.size === 0) {
          // all done.
          this.done = true;
          this._resolve();
        }
      });
    }
  }

  push (...funcs) {
    funcs.forEach(func => {
      this._add(func);
    });
  }

  unshift (...funcs) {
    funcs.forEach(func => {
      this._add(func, false);
    });
  }

  wait () {
    return this._promise;
  }
}

class Worker extends EventEmitter {
  constructor (parent) {
    super();
    this.parent = parent;
    setImmediate(() => this.work());
  }

  async work () {
    while (true) {
      if (this.parent.queue.length) {
        let job = this.parent.queue.shift();
        try {
          await job();
          this.parent.emit('complete', job);
        } catch (err) {
          this.parent.emit('error', err, job);
        }
      } else {
        setImmediate(() => this.emit('terminate'));
        break;
      }
    }
  }
}

module.exports = PromiseQueue;
